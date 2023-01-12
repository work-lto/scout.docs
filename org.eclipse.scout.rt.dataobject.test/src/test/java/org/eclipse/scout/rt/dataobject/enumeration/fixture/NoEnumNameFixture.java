/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.rt.dataobject.enumeration.fixture;

import org.eclipse.scout.rt.dataobject.enumeration.IEnum;

public enum NoEnumNameFixture implements IEnum {

  A("a"),
  B("b"),
  C("c");

  private final String m_stringValue;

  NoEnumNameFixture(String stringValue) {
    m_stringValue = stringValue;
  }

  @Override
  public String stringValue() {
    return m_stringValue;
  }
}
