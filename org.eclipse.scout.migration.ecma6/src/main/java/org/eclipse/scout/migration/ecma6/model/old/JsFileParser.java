package org.eclipse.scout.migration.ecma6.model.old;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.scout.migration.ecma6.Configuration;
import org.eclipse.scout.migration.ecma6.PathFilters;
import org.eclipse.scout.migration.ecma6.WorkingCopy;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsFileParser {

  private static final Logger LOG = LoggerFactory.getLogger(JsFileParser.class);

  private static Pattern START_COPY_RIGHT = Pattern.compile("^/\\*{1}$");
  private static Pattern END_COPY_RIGHT = Pattern.compile("^ \\*{1}/$");

  private static Pattern START_FUNCTION_COMMENT = Pattern.compile("^/\\*\\*$");
  private static Pattern FUNCTION_COMMENT = Pattern.compile("^ \\*");
  private static Pattern END_FUNCTION_COMMENT = Pattern.compile("^ \\*(\\s*\\*)?/");

  /**
   * <pre>
    * scout.Device = function(model) {
    * Groups:
    *  1 indent (empty)
   *  2 namespace = scope.Device
   *  3 field name (empty)
    *  4 params
    *  5 open bracket
    *  6 closing bracket (opt)
   * </pre>
   */
  private static Pattern START_CONSTRUCTOR = Pattern.compile("^()([^ .]+\\.[^ .]+)()\\s*=\\s*function\\(([^)]*)\\)\\s*(\\{)\\s*(\\}\\;)?");

  /**
   * <pre>
   * scout.strings = {
   * Groups:
   *  1 indent (empty)
   *  2 namespace = scope.class
   *  3 field name (empty)
   *  4 params (empty)
   *  5 open bracket
   *  6 closing bracket (opt)
   * </pre>
   */
  private static Pattern START_UTILITY_CONSTRUCTOR = Pattern.compile("^()([^ .]+\\.[a-z][^ .]+)()\\s*=\\s*()(\\{)\\s*(\\}\\;)?");

  /**
   * <pre>
   * scout.KeyStroke.Mode = {
   * Groups:
   *  1 indent
   *  2 namespace = scope.class
   *  3 field name
   *  4 open bracket
   *  5 closing bracket (opt)
   * </pre>
   */
  private static Pattern START_ENUM = Pattern.compile("^(\\s*)([^ .]+\\.[^ .]+)\\.([A-Z][^ .]+)\\s*=\\s*(\\{)\\s*(\\}\\;)?");

  /**
   * <pre>
   * scout.TreeVisitResult = {
   * Groups:
   *  1 indent (empty)
   *  2 namespace = scope
   *  3 field name
   *  4 open bracket
   *  5 closing bracket (opt)
   * </pre>
   */
  private static Pattern START_TOP_LEVEL_ENUM = Pattern.compile("^()([^ .]+)\\.([^ .]+)\\s*=\\s*(\\{)\\s*(\\}\\;)?");

  /**
   * <pre>
    * Groups:
    *  1 indent (empty)
   *  2 namespace = scope.class
    *  3 name
    *  4 params
    *  5 open bracket
    *  6 closing bracket (opt)
   * </pre>
   */
  private static Pattern START_FUNCTION = Pattern.compile("^()([^ .]+\\.[^ .]+)\\.prototype\\.([^ .]+)\\s*=\\s*function\\(([^)]*)\\)\\s*(\\{)\\s*(\\}\\;)?");

  /**
   * <pre>
    * Groups:
    *  1 indent (empty)
   *  2 namespace = scope.class
   *  3 field name
    *  4 params
    *  5 open bracket
    *  6 closing bracket (opt)
   * </pre>
   */
  private static Pattern START_STATIC_FUNCTION = Pattern.compile("^()([^ .]+\\.[^ .]+)\\.([^ .]+)\\s*=\\s*function\\(([^)]*)\\)\\s*(\\{)\\s*(\\}\\;)?");

  /**
   * <pre>
    * SPACE SPACE insertAt: function(text, insertText, position) {
    * Groups:
    *  1 indent (non-empty)
    *  2 namespace = scope.class (empty)
   *  3 field name
    *  4 params
    *  5 open bracket
    *  6 closing bracket (opt)
   * </pre>
   */
  private static Pattern START_UTILITY_FUNCTION = Pattern.compile("^(\\s*)()([_a-z][^ .]+)\\s*:\\s*function\\(([^)]*)\\)\\s*(\\{)\\s*(\\}\\,?)?");

  /**
   * <pre>
   * Groups:
   *  1 namespace = scope.class
   *  2 field name
   *  3 allocation (opt)
   *  4 delimiter semicolumn (opt)
   * </pre>
   */
  private static Pattern START_CONSTANT = Pattern.compile("^([^. ]+\\.[^ .]+)\\.([A-Z0-9_]+)(\\s*=\\s*.*)?(;)$");

  /**
   * <pre>
   * Groups:
   *  none
   * </pre>
   */
  private static Pattern START_APP_LISTENER = Pattern.compile("^" + Pattern.quote("scout.addAppListener('") + "(bootstrap|prepare)" + Pattern.quote("', function() {") + "$");

  private static Pattern END_APP_LISTENER = Pattern.compile("\\}\\)\\;");

  /**
   * <pre>
    * Groups:
    * 1 indent
    * 2 closing bracket
    * 3 termination character ';' or ','
   * </pre>
   */
  private static Pattern END_BLOCK = Pattern.compile("^(\\s*)(\\}\\s*)([;,])");

  private static Pattern SUPER_BLOCK = Pattern.compile("scout\\.inherits\\(([^,]+),\\s*([^,]+)\\);");

  private WorkingCopy m_workingCopy;
  private final JsFile m_jsFile;
  private BufferedReader m_sourceReader;
  private String m_currentLine;
  private int m_currentLineNumber = 0;
  private int m_offsetStartLine = 0;
  private final String m_lineSeparator;
  private String m_utilityFunctionNamespace;

  public JsFileParser(WorkingCopy workingCopy) {
    m_workingCopy = workingCopy;
    String source = workingCopy.getInitialSource();
    m_lineSeparator = workingCopy.getLineSeparator();
    m_sourceReader = new BufferedReader(new StringReader(source));
    m_jsFile = new JsFile(workingCopy.getPath());
  }

  public JsFile parse() throws IOException {
    JsFunction instanceGetter = null;
    Matcher matcher = null;
    try {
      nextLine();
      JsCommentBlock comment = null;
      while (m_currentLine != null) {

        if (m_currentLineNumber == 1) {
          matcher = START_COPY_RIGHT.matcher(m_currentLine);
          if (matcher.find()) {
            readCopyRight();
            continue;
          }
        }
        matcher = START_FUNCTION_COMMENT.matcher(m_currentLine);
        if (matcher.find()) {
          comment = readFunctionComment();
          continue;
        }
        matcher = START_CONSTRUCTOR.matcher(m_currentLine);
        if (matcher.find() && PathFilters.isClass().test(m_jsFile.getPathInfo())) {
          readFunction(matcher, comment, true, false);
          comment = null;
          continue;
        }
        matcher = START_ENUM.matcher(m_currentLine);
        if (matcher.find()) {
          readEnum(matcher);
          continue;
        }
        matcher = START_TOP_LEVEL_ENUM.matcher(m_currentLine);
        if (matcher.find() && PathFilters.isTopLevelEnum().test(m_jsFile.getPathInfo())) {
          readTopLevelEnum(matcher);
          continue;
        }
        matcher = START_UTILITY_CONSTRUCTOR.matcher(m_currentLine);
        if (matcher.find() && PathFilters.isUtility().test(m_jsFile.getPathInfo())) {
          readUtilityFunction(matcher, comment, true);
          comment = null;
          continue;
        }
        matcher = START_FUNCTION.matcher(m_currentLine);
        if (matcher.find()) {
          readFunction(matcher, comment, false, false);
          comment = null;
          continue;
        }
        matcher = START_STATIC_FUNCTION.matcher(m_currentLine);
        if (matcher.find()) {
          readFunction(matcher, comment, false, true);
          comment = null;
          continue;
        }
        matcher = START_UTILITY_FUNCTION.matcher(m_currentLine);
        if (matcher.find() && PathFilters.isUtility().test(m_jsFile.getPathInfo())) {
          readUtilityFunction(matcher, comment, false);
          comment = null;
          continue;
        }
        matcher = START_CONSTANT.matcher(m_currentLine);
        if (matcher.find()) {
          readConstant(matcher);
          continue;
        }
        matcher = SUPER_BLOCK.matcher(m_currentLine);
        if (matcher.find()) {
          JsClass clazz = m_jsFile.getLastOrAppend(matcher.group(1));
          clazz.setSuperCall(readSuperCall(matcher));
          continue;
        }
        matcher = START_APP_LISTENER.matcher(m_currentLine);
        if (matcher.find()) {
          instanceGetter = readAppListener();
          continue;
        }
        nextLine();
      }
    }
    catch (VetoException e) {
      LOG.error("Could not parse file '" + m_jsFile.getPath().getFileName() + ":" + m_currentLineNumber + "'.", e);
      throw e;
    }

    List<JsClass> jsClasses = m_jsFile.getJsClasses();
    if (jsClasses.size() == 1) {
      jsClasses.get(0).setDefault(true);
    }
    List<JsTopLevelEnum> jsTopLevelEnums = m_jsFile.getJsTopLevelEnums();
    if (instanceGetter != null) {
      if (jsClasses.size() == 1) {
        instanceGetter.setJsClass(jsClasses.get(0));
        jsClasses.get(0).addFunction(instanceGetter);
      }
      else {
        LOG.error("Could not create instance getter of singleton '" + m_jsFile.getPath() + "'");
      }
    }
    // log
    if (jsClasses.size() == 0 && jsTopLevelEnums.size() == 0) {
      LOG.error("No classes found in file '" + m_jsFile.getPath().getFileName() + "'.");
    }
    else if (jsClasses.size() > 1) {
      LOG.warn("More than 1 class found in file '" + m_jsFile.getPath().getFileName() + "'. Every classfile should be defined in its own file.");
    }
    return m_jsFile;
  }

  private void readCopyRight() throws IOException {
    JsCommentBlock comment = new JsCommentBlock();
    StringBuilder commentBody = new StringBuilder(m_currentLine).append(m_lineSeparator);
    nextLine();

    while (m_currentLine != null) {
      if (END_COPY_RIGHT.matcher(m_currentLine).find()) {
        commentBody.append(m_currentLine).append(m_lineSeparator);
        break;
      }
      else if (FUNCTION_COMMENT.matcher(m_currentLine).find()) {
        commentBody.append(m_currentLine).append(m_lineSeparator);
      }
      else {
        // no comment
        throw new VetoException("Function commentblock could not be parsed (" + m_workingCopy.getPath().getFileName() + ":" + m_currentLineNumber + ") [line: '" + m_currentLine + "']! ");
      }
      nextLine();
    }
    comment.setSource(commentBody.toString());
    m_jsFile.setCopyRight(comment);
    nextLine();
  }

  private JsCommentBlock readFunctionComment() throws IOException {
    JsCommentBlock comment = new JsCommentBlock();
    StringBuilder commentBody = new StringBuilder(m_currentLine).append(m_lineSeparator);
    nextLine();

    while (m_currentLine != null) {
      if (END_FUNCTION_COMMENT.matcher(m_currentLine).find()) {
        commentBody.append(m_currentLine).append(m_lineSeparator);
        break;
      }
      else if (FUNCTION_COMMENT.matcher(m_currentLine).find()) {
        commentBody.append(m_currentLine).append(m_lineSeparator);
      }
      else {
        // no comment
        throw new VetoException("Function commentblock could not be parsed (" + m_workingCopy.getPath().getFileName() + ":" + m_currentLineNumber + ") [line: '" + m_currentLine + "']! ");
      }
      nextLine();
    }
    comment.setSource(commentBody.toString());
    nextLine();
    return comment;
  }

  private JsFunction readFunction(Matcher matcher, JsCommentBlock comment, boolean constructor, boolean isStatic) throws IOException {
    String indent = matcher.group(1);
    JsClass clazz = m_jsFile.getLastOrAppend(matcher.group(2));
    JsFunction function = new JsFunction(clazz, matcher.group(3));
    function.setComment(comment);
    function.setConstructor(constructor);
    function.setStatic(isStatic);
    function.setArgs(matcher.group(4));
    StringBuilder functionBody = new StringBuilder(m_currentLine).append(m_lineSeparator);
    if (StringUtility.hasText(matcher.group(6))) {
      function.setSource(functionBody.toString());
      clazz.addFunction(function);
      nextLine();
      return function;
    }
    nextLine();
    while (m_currentLine != null) {
      Matcher endMatcher = END_BLOCK.matcher(m_currentLine);
      if (endMatcher.matches() && endMatcher.group(1).equals(indent)) {
        functionBody.append(endMatcher.group(1)+endMatcher.group(2));
        break;
      }

      if (StringUtility.hasText(m_currentLine) && !m_currentLine.startsWith(" ")) {
        throw new VetoException("Could not parse function body (" + m_workingCopy.getPath().getFileName() + ":" + m_currentLineNumber + ")");
      }
      functionBody.append(m_currentLine).append(m_lineSeparator);
      nextLine();
    }
    function.setSource(functionBody.toString());

    clazz.addFunction(function);
    return function;
  }

  private JsFunction readUtilityFunction(Matcher matcher, JsCommentBlock comment, boolean constructor) throws IOException {
    String namespace = matcher.group(2);
    if (constructor) {
      m_utilityFunctionNamespace = namespace;
    }
    else if (m_utilityFunctionNamespace == null) {
      LOG.warn("wrong utility-style function detected. {}: {}", m_workingCopy.getPath(), matcher.group(3));
      nextLine();
      return null;
    }
    else {
      namespace = m_utilityFunctionNamespace;
    }
    String indent = matcher.group(1);
    JsClass clazz = m_jsFile.getLastOrAppend(namespace);
    JsFunction function = new JsFunction(clazz, matcher.group(3));
    function.setComment(comment);
    function.setConstructor(constructor);
    function.setStatic(true);
    function.setArgs(matcher.group(4));
    function.setSource("{}");
    clazz.addFunction(function);
    nextLine();
    return function;
  }

  protected JsEnum readEnum(Matcher matcher) throws IOException {
    String indent = matcher.group(1);
    JsClass clazz = m_jsFile.getLastOrAppend(matcher.group(2));
    JsEnum jsEnum = new JsEnum(clazz, matcher.group(3));
    StringBuilder bodyBuilder = new StringBuilder(matcher.group(4));
    if (StringUtility.hasText(matcher.group(5))) {
      // take care dynamic values can not be implemented as cons
      LOG.warn("Dynamic enum '" + jsEnum.getName() + "' found in " + m_workingCopy.getPath().getFileName() + ":" + m_currentLineNumber);
      jsEnum.addParseError("Looks like a dynamic jsEnum. Must be migrated by hand.");
      clazz.addEnum(jsEnum);
      nextLine();
      return jsEnum;
    }
    nextLine();
    while (m_currentLine != null) {
      Matcher endMatcher = END_BLOCK.matcher(m_currentLine);
      if (endMatcher.matches() && endMatcher.group(1).equals(indent)) {
        bodyBuilder.append("}");
        break;
      }
      bodyBuilder.append(m_currentLine);
      if (StringUtility.hasText(m_currentLine) && !m_currentLine.startsWith(" ")) {
        throw new VetoException("Could not parse enum body (" + m_workingCopy.getPath().getFileName() + ":" + m_currentLineNumber + ")");
      }
      nextLine();
    }
    jsEnum.setSource(bodyBuilder.toString());

    clazz.addEnum(jsEnum);
    return jsEnum;
  }

  protected JsTopLevelEnum readTopLevelEnum(Matcher matcher) throws IOException {
    String indent = "";
    JsTopLevelEnum jsEnum = new JsTopLevelEnum(matcher.group(2), matcher.group(3), m_jsFile);
    StringBuilder bodyBuilder = new StringBuilder(matcher.group(4));
    if (StringUtility.hasText(matcher.group(5))) {
      // take care dynamic values can not be implemented as cons
      LOG.warn("Dynamic enum '" + jsEnum.getName() + "' found in " + m_workingCopy.getPath().getFileName() + ":" + m_currentLineNumber);
      jsEnum.addParseError("Looks like a dynamic jsEnum. Must be migrated by hand.");
      m_jsFile.addJsTopLevelEnum(jsEnum);
      nextLine();
      return jsEnum;
    }
    nextLine();
    while (m_currentLine != null) {
      Matcher endMatcher = END_BLOCK.matcher(m_currentLine);
      if (endMatcher.matches() && endMatcher.group(1).equals(indent)) {
        bodyBuilder.append(endMatcher.group(1)+endMatcher.group(2));
        break;
      }
      bodyBuilder.append(m_currentLine).append(m_lineSeparator);
      if (StringUtility.hasText(m_currentLine) && !m_currentLine.startsWith(" ")) {
        throw new VetoException("Could not parse enum body (" + m_workingCopy.getPath().getFileName() + ":" + m_currentLineNumber + ")");
      }
      nextLine();
    }
    jsEnum.setSource(bodyBuilder.toString());
    m_jsFile.addJsTopLevelEnum(jsEnum);
    return jsEnum;
  }

  protected JsConstant readConstant(Matcher matcher) throws IOException {
    JsClass clazz = m_jsFile.getLastOrAppend(matcher.group(1));
    JsConstant constant = new JsConstant(clazz, matcher.group(2));
    constant.setSource(matcher.group(3));
    nextLine();
    clazz.addConstant(constant);
    return constant;

  }

  protected JsFunction readAppListener() throws IOException {
    JsAppListener appListener = new JsAppListener(m_jsFile);
    StringBuilder functionBody = new StringBuilder(m_currentLine).append(m_lineSeparator);
    Pattern namePattern = Pattern.compile("(" + Configuration.get().getNamespace() + "\\.[^ \\=]*)\\s*\\=\\s*scout\\.create\\(");

    nextLine();
    while (m_currentLine != null) {
      Matcher endMatcher = END_APP_LISTENER.matcher(m_currentLine);
      if (endMatcher.matches()) {
        functionBody.append(m_currentLine);
        break;
      }
      Matcher nameMatcher = namePattern.matcher(m_currentLine);
      if (nameMatcher.find()) {
        if (appListener.getInstanceName() != null) {
          LOG.error("Found more than one assignments in appListener of '" + m_jsFile.getPath() + "'.");
          appListener.addParseError("Found more than one assignments in appListener - hand migration required!");
        }
        appListener.setInstanceFqn(nameMatcher.group(1));
      }
      functionBody.append(m_currentLine).append(m_lineSeparator);
      if (StringUtility.hasText(m_currentLine) && !m_currentLine.startsWith(" ")) {
        throw new VetoException("Could not parse appListener body (" + m_workingCopy.getPath().getFileName() + ":" + m_currentLineNumber + ")");
      }
      nextLine();
    }
    // skip when no assignment has been found.
    if (appListener.getInstanceName() == null) {
      return null;
    }
    appListener.setSource(functionBody.toString());

    m_jsFile.addAppListener(appListener);

    // create instance getter function
    StringBuilder instanceGetterBuilder = new StringBuilder();
    instanceGetterBuilder.append("static get() {").append(m_lineSeparator)
        .append("  return instance;").append(m_lineSeparator)
        .append("}").append(m_lineSeparator);

    JsFunction instanceGetter = new JsFunction(null, "get");
    instanceGetter.setMemoryOnly(true);
    instanceGetter.setStatic(true);
    instanceGetter.addSingletonReference(appListener.getInstanceNamespace() + "." + appListener.getInstanceName());
    instanceGetter.setSource(instanceGetterBuilder.toString());

    return instanceGetter;

  }

  protected JsSuperCall readSuperCall(Matcher matcher) throws IOException {
    JsSuperCall superCall = new JsSuperCall(matcher.group(2));
    superCall.setSource(matcher.group());
    nextLine();
    return superCall;
  }

  private void nextLine() throws IOException {
    if (m_currentLine != null) {
      m_offsetStartLine += (m_currentLine.length() + m_lineSeparator.length());
    }
    m_currentLine = m_sourceReader.readLine();
    m_currentLineNumber++;
  }

}
