/*
 * Copyright (c) 2010, 2024 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.contacts.shared.common;

import jakarta.annotation.Generated;

import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@ClassId("73a4276f-77b2-4ad2-b414-7f806284bdb3-formdata")
@Generated(value = "org.eclipse.scout.contacts.client.common.AbstractUrlImageField", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public abstract class AbstractUrlImageFieldData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  /**
   * access method for property Url.
   */
  public String getUrl() {
    return getUrlProperty().getValue();
  }

  /**
   * access method for property Url.
   */
  public void setUrl(String url) {
    getUrlProperty().setValue(url);
  }

  public UrlProperty getUrlProperty() {
    return getPropertyByClass(UrlProperty.class);
  }

  public static class UrlProperty extends AbstractPropertyData<String> {
    private static final long serialVersionUID = 1L;
  }
}
