package org.eclipse.scout.rt.ui.html.res;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.scout.commons.FileUtility;
import org.eclipse.scout.commons.IOUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.ui.html.AbstractRequestHandler;
import org.eclipse.scout.rt.ui.html.AbstractScoutAppServlet;

/**
 * serve a file as a servlet resource using caches
 */
public class StaticResourceHandler extends AbstractRequestHandler {
  private static final long serialVersionUID = 1L;
  private static final IScoutLogger LOG = ScoutLogManager.getLogger(StaticResourceHandler.class);
  private static final String LAST_MODIFIED = "Last-Modified"; //$NON-NLS-1$
  private static final String IF_MODIFIED_SINCE = "If-Modified-Since"; //$NON-NLS-1$
  private static final String IF_NONE_MATCH = "If-None-Match"; //$NON-NLS-1$
  private static final int IF_MODIFIED_SINCE_DELTA = 999;
  private static final String ETAG = "ETag"; //$NON-NLS-1$
  private static final int ANY_SIZE = 8192;

  public StaticResourceHandler(AbstractScoutAppServlet servlet, HttpServletRequest req, HttpServletResponse resp, String pathInfo) {
    super(servlet, req, resp, pathInfo);
  }

  @Override
  public boolean handle() throws ServletException, IOException {
    HttpServletRequest req = getHttpServletRequest();
    HttpServletResponse resp = getHttpServletResponse();
    URL url = getServlet().getResourceLocator().getWebContentResource(getPathInfo());

    URLConnection connection = url.openConnection();
    long lastModified = connection.getLastModified();
    int contentLength = connection.getContentLength();
    int status = processCacheHeaders(lastModified, contentLength);
    if (status == HttpServletResponse.SC_NOT_MODIFIED) {
      resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      return true;
    }

    //Return file regularly if the client (browser) does not already have it or if the file has changed in the meantime
    byte[] content = fileContent(url);
    resp.setContentLength(content.length);

    //Prefer mime type mapping from container
    String path = url.getPath();
    int lastSlash = path.lastIndexOf('/');
    int lastDot = path.lastIndexOf('.');
    String fileName = lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
    String fileExtension = lastDot >= 0 ? path.substring(lastDot + 1) : path;

    String contentType = getServlet().getServletContext().getMimeType(fileName);
    if (contentType == null) {
      contentType = getMsOfficeMimeTypes(fileExtension);
    }
    if (contentType == null) {
      contentType = FileUtility.getContentTypeForExtension(fileExtension);
    }
    if (contentType == null) {
      LOG.warn("Could not determine content type of file " + path);
    }
    else {
      resp.setContentType(contentType);
    }

    resp.getOutputStream().write(content);
    return true;
  }

  /**
   * TODO AWE: (scout) In org.eclipse.scout.commons.FileUtility hinzufügen
   * see: http://stackoverflow.com/questions/4212861/what-is-a-correct-mime-type-for-docx-pptx-etc
   */
  private static final Map<String, String> EXT_TO_MIME_TYPE_MAP = new HashMap<>();

  static {
    EXT_TO_MIME_TYPE_MAP.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    EXT_TO_MIME_TYPE_MAP.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
    EXT_TO_MIME_TYPE_MAP.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
    EXT_TO_MIME_TYPE_MAP.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
    EXT_TO_MIME_TYPE_MAP.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    EXT_TO_MIME_TYPE_MAP.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    EXT_TO_MIME_TYPE_MAP.put("sldx", "application/vnd.openxmlformats-officedocument.presentationml.slide");
    EXT_TO_MIME_TYPE_MAP.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    EXT_TO_MIME_TYPE_MAP.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    EXT_TO_MIME_TYPE_MAP.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
    EXT_TO_MIME_TYPE_MAP.put("xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
    EXT_TO_MIME_TYPE_MAP.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
  }

  private String getMsOfficeMimeTypes(String fileExtension) {
    return EXT_TO_MIME_TYPE_MAP.get(fileExtension.toLowerCase());
  }

  /**
   * Checks whether the file needs to be returned or not, depending on the request headers and file modification state.
   * Also writes cache headers (last modified and etag) if the file needs to be returned.
   *
   * @return {@link HttpServletResponse#SC_NOT_MODIFIED} if the file hasn't changed in the meantime or
   *         {@link HttpServletResponse#SC_ACCEPTED} if the content of the file needs to be returned.
   */
  protected int processCacheHeaders(long lastModified, int contentLength) {
    HttpServletRequest req = getHttpServletRequest();
    HttpServletResponse resp = getHttpServletResponse();

    resp.setHeader("cache-control", "private, max-age=0, no-cache, no-store, must-revalidate");//FIXME imo
    //resp.setHeader("cache-control", "public, max-age=240, s-maxage=240");

    String etag = null;
    if (lastModified != -1L && contentLength != -1L) {
      etag = "W/\"" + contentLength + "-" + lastModified + "\""; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
    }

    // Check for cache revalidation.
    // We should prefer ETag validation as the guarantees are stronger and all
    // HTTP 1.1 clients should be using it
    String ifNoneMatch = req.getHeader(IF_NONE_MATCH);
    if (notModified(ifNoneMatch, etag)) {
      return HttpServletResponse.SC_NOT_MODIFIED;
    }
    else {
      long ifModifiedSince = req.getDateHeader(IF_MODIFIED_SINCE);
      // for purposes of comparison we add 999 to ifModifiedSince since the
      // fidelity
      // of the IMS header generally doesn't include milli-seconds
      if (notModifiedSince(ifModifiedSince, lastModified)) {
        return HttpServletResponse.SC_NOT_MODIFIED;
      }
    }

    // File needs to be returned regularly, write cache headers
    if (lastModified > 0) {
      resp.setDateHeader(LAST_MODIFIED, lastModified);
    }
    if (etag != null) {
      resp.setHeader(ETAG, etag);
    }

    return HttpServletResponse.SC_ACCEPTED;
  }

  protected boolean notModified(String ifNoneMatch, String etag) {
    return (ifNoneMatch != null && etag != null && ifNoneMatch.indexOf(etag) != -1);
  }

  protected boolean notModifiedSince(long ifModifiedSince, long lastModified) {
    return (ifModifiedSince > -1 && lastModified > 0 && lastModified <= (ifModifiedSince + IF_MODIFIED_SINCE_DELTA));
  }

  protected byte[] fileContent(URL url) throws IOException {
    try (InputStream in = url.openStream()) {
      return IOUtility.getContent(in);
    }
    catch (ProcessingException e) {
      throw new IOException(e.getMessage());
    }
  }

}
