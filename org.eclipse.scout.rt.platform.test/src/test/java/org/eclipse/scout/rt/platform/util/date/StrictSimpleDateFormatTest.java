/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.rt.platform.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.eclipse.scout.rt.platform.holders.StringHolder;
import org.junit.Assert;
import org.junit.Test;

public class StrictSimpleDateFormatTest {

  /**
   * Asserts the JavaDoc examples of {@link StrictSimpleDateFormat}
   */
  @Test
  public void testJavaDoc() throws ParseException {
    StringHolder pattern = new StringHolder();
    StringHolder input = new StringHolder();

    pattern.setValue("yyyy-MM-dd HH:mm:ss.SSS");
    input.setValue("2019-01-18");
    Assert.assertThrows(ParseException.class, () -> new SimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue()));

    pattern.setValue("yyyy-MM-dd");
    input.setValue("2019-18");
    Assert.assertThrows(ParseException.class, () -> new SimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue()));

    pattern.setValue("yyyy-MM-dd");
    input.setValue("2019-1-18");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue()));

    pattern.setValue("yyyyMMdd");
    input.setValue("20190118xyz");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue()));

    pattern.setValue("yyyy-MM-dd");
    input.setValue("2019-01-18 23:00:00.000");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue()));

    pattern.setValue("yyyy/yyyy");
    input.setValue("2018/2019");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue()));
  }

  /**
   * Asserts that strings generated by JavaScript when serializing <i>Date</i> objects to JSON are rejected by
   * {@link StrictSimpleDateFormat}.
   *
   * @see <a href=
   *      "https://developer.mozilla.org/de/docs/Web/JavaScript/Reference/Global_Objects/Date/toJSON">Date.toJSON()</a>
   */
  @Test
  public void testJavaScriptJsonString() throws ParseException {
    final StringHolder pattern = new StringHolder();
    final String input = "2019-01-18T12:42:03.409Z";

    pattern.setValue("yyyy-MM-dd HH:mm:ss.SSS");
    Assert.assertThrows(ParseException.class, () -> new SimpleDateFormat(pattern.getValue()).parse(input));
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input));

    pattern.setValue("yyyy-MM-dd HH:mm:ss.SSS Z");
    Assert.assertThrows(ParseException.class, () -> new SimpleDateFormat(pattern.getValue()).parse(input));
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input));

    pattern.setValue("yyyy-MM-dd");
    new SimpleDateFormat(pattern.getValue()).parse(input);
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input));
  }

  @Test
  public void testWithTimeZone_DefaultTZ() throws ParseException {
    testWithTimeZoneImpl(); // Default time zone
  }

  @Test
  public void testWithTimeZone_CET() throws ParseException {
    TimeZone backupTz = TimeZone.getDefault();
    try {
      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      testWithTimeZoneImpl();
    }
    finally {
      TimeZone.setDefault(backupTz);
    }
  }

  @Test
  public void testWithTimeZone_CEST() throws ParseException {
    TimeZone backupTz = TimeZone.getDefault();
    try {
      TimeZone.setDefault(TimeZone.getTimeZone("CEST"));
      testWithTimeZoneImpl();
    }
    finally {
      TimeZone.setDefault(backupTz);
    }
  }

  @Test
  public void testWithTimeZone_UTC() throws ParseException {
    TimeZone backupTz = TimeZone.getDefault();
    try {
      TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
      testWithTimeZoneImpl();
    }
    finally {
      TimeZone.setDefault(backupTz);
    }
  }

  @Test
  public void testWithTimeZone_PST() throws ParseException {
    TimeZone backupTz = TimeZone.getDefault();
    try {
      TimeZone.setDefault(TimeZone.getTimeZone("PST")); // UTC−08:00
      testWithTimeZoneImpl();
    }
    finally {
      TimeZone.setDefault(backupTz);
    }
  }

  protected void testWithTimeZoneImpl() throws ParseException {
    StringHolder pattern = new StringHolder();
    StringHolder input = new StringHolder();

    pattern.setValue("yyyy-MM-dd HH:mm:ss.SSS Z");
    input.setValue("2019-01-18");
    Assert.assertThrows(ParseException.class, () -> new SimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    input.setValue("2019-01-18 23:00:00.000");
    Assert.assertThrows(ParseException.class, () -> new SimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    input.setValue("2019-01-18 23:00:00.000 +0700");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());
    input.setValue("2019-01-18 23:00:00.000 GMT-02:30");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());
    input.setValue("1990-12-31 00:00:00.000 +0500");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());

    pattern.setValue("yyyy-MM-dd Z");
    input.setValue("2019-01-18");
    Assert.assertThrows(ParseException.class, () -> new SimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    input.setValue("2019-01-18 +0700");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());
    input.setValue("2019-01-18 GMT-02:30");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());

    // Daylight saving time
    pattern.setValue("yyyy-MM-dd HH:mm:ss.SSS Z");
    input.setValue("2021-03-28 00:29:00.000 +0000");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());
    input.setValue("2021-03-28 01:29:00.000 +0000");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());
    input.setValue("2021-03-28 04:29:00.000 +0000");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());
    input.setValue("2021-10-31 00:29:00.000 +0000");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());
    input.setValue("2021-10-31 01:29:00.000 +0000");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());
    input.setValue("2021-10-31 04:29:00.000 +0000");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());

    // Correctly handle literal strings (even if they contain the letter 'Z')
    pattern.setValue("'''$''' '-' yyyy-MM-dd '(A-Z)' HH:mm:ss.SSS Z");
    input.setValue("'$' - 2019-01-18 (A-Z)");
    Assert.assertThrows(ParseException.class, () -> new SimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    input.setValue("'$' - 2019-01-18 (A-Z) 23:00:00.000");
    Assert.assertThrows(ParseException.class, () -> new SimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    Assert.assertThrows(ParseException.class, () -> new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue()));
    input.setValue("'$' - 2019-01-18 (A-Z) 23:00:00.000 +0700");
    new SimpleDateFormat(pattern.getValue()).parse(input.getValue());
    new StrictSimpleDateFormat(pattern.getValue()).parse(input.getValue());
  }
}
