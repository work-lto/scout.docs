/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.rt.client.ui.form.fields;

import org.eclipse.scout.rt.client.ui.IWidget;

public interface IWidgetField<WIDGET extends IWidget> extends IFormField {

  String PROP_SCROLLABLE = "scrollable";
  String PROP_FIELD_WIDGET = "fieldWidget";

  boolean isScrollable();

  void setScrollable(boolean scrollable);

  WIDGET getFieldWidget();

  void setFieldWidget(WIDGET fieldWidget);
}
