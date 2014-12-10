/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.rt.ui.html.json.fixtures;

import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.ui.html.json.IJsonAdapter;
import org.eclipse.scout.rt.ui.html.json.IJsonSession;
import org.eclipse.scout.rt.ui.html.json.desktop.JsonDesktop;
import org.json.JSONObject;

public class JsonDesktopMock extends JsonDesktop<IDesktop> {

  public JsonDesktopMock(IDesktop model, IJsonSession jsonSession, String id, IJsonAdapter<?> parent) {
    super(model, jsonSession, id, parent);
  }

  @Override
  public void init() {
  }

  @Override
  protected void attachChildAdapters() {
  }

  @Override
  protected void attachModel() {
  }

  @Override
  public JSONObject toJson() {
    return new JSONObject();
  }

}
