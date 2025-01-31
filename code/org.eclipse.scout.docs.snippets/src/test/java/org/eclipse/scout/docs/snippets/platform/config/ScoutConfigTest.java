/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.docs.snippets.platform.config;

import static org.junit.Assert.assertEquals;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.config.PlatformConfigProperties.ApplicationNameProperty;
import org.junit.Test;

public class ScoutConfigTest {

  @Test
  public void testMyCustomTimeoutProperty() {
    //Beans defined in Test needs to be registered:
    BEANS.getBeanManager().registerClass(MyCustomTimeoutProperty.class);

    //tag::getPropertyValue[]
    Long value = CONFIG.getPropertyValue(MyCustomTimeoutProperty.class);
    //end::getPropertyValue[]

    Long expected = 3600L;
    assertEquals(expected, value);

    BEANS.getBeanManager().unregisterClass(MyCustomTimeoutProperty.class);
  }

  @Test
  public void testApplicationNamePropertyDefault() {
    String value = CONFIG.getPropertyValue(ApplicationNameProperty.class);
    assertEquals("My Scout Application", value);
  }

  @Test
  public void testApplicationNameConstant() {
    BEANS.getBeanManager().registerClass(ApplicationNameConstant.class);

    String value = CONFIG.getPropertyValue(ApplicationNameProperty.class);
    assertEquals("Contacts Application", value);

    BEANS.getBeanManager().unregisterClass(ApplicationNameConstant.class);
  }

  @Test
  public void testApplicationNamePropertyRedirection() {
    BEANS.getBeanManager().registerClass(ApplicationNamePropertyRedirection.class);

    String value = CONFIG.getPropertyValue(ApplicationNameProperty.class);
    assertEquals("My Project Application", value);

    BEANS.getBeanManager().unregisterClass(ApplicationNamePropertyRedirection.class);
  }
}
