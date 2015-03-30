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
package org.eclipse.scout.rt.ui.html.json.form.fields.smartfield;

import org.eclipse.scout.commons.TriState;
import org.eclipse.scout.commons.status.IStatus;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.IProposalChooser;
import org.eclipse.scout.rt.ui.html.json.AbstractJsonPropertyObserver;
import org.eclipse.scout.rt.ui.html.json.IJsonAdapter;
import org.eclipse.scout.rt.ui.html.json.IJsonSession;
import org.eclipse.scout.rt.ui.html.json.JsonEvent;
import org.eclipse.scout.rt.ui.html.json.JsonProperty;
import org.eclipse.scout.rt.ui.html.json.JsonStatus;
import org.eclipse.scout.rt.ui.html.json.form.fields.JsonAdapterProperty;
import org.json.JSONObject;

public class JsonProposalChooser extends AbstractJsonPropertyObserver<IProposalChooser> {

  public JsonProposalChooser(IProposalChooser model, IJsonSession jsonSession, String id, IJsonAdapter<?> parent) {
    super(model, jsonSession, id, parent);
  }

  @Override
  public String getObjectType() {
    return "ProposalChooser";
  }

  @Override
  protected void initJsonProperties(IProposalChooser model) {
    super.initJsonProperties(model);
    // We don't support that a smart-field could change the activeStateFilterEnabled property at runtime
    // Because very few smart-fields do have such a filter we'd rather not send the additional JSON data
    // used to render the filter in the UI for each smart-field.
    putJsonProperty(new JsonAdapterProperty<IProposalChooser>("model", model, getJsonSession()) {
      @Override
      protected Object modelValue() {
        return getModel().getModel();
      }
    });
    putJsonProperty(new JsonProperty<IProposalChooser>(IProposalChooser.PROP_STATUS, model) {
      @Override
      protected IStatus modelValue() {
        return getModel().getStatus();
      }

      @Override
      public Object prepareValueForToJson(Object value) {
        return JsonStatus.toJson((IStatus) value);
      }
    });
    putJsonProperty(new JsonProperty<IProposalChooser>(IProposalChooser.PROP_STATUS_VISIBLE, model) {
      @Override
      protected Boolean modelValue() {
        return getModel().isStatusVisible();
      }
    });
  }

  @Override
  protected void attachChildAdapters() {
    super.attachChildAdapters();
    attachAdapter(getModel().getModel());
  }

  @Override
  public void handleUiEvent(JsonEvent event) {
    if ("activeFilterChanged".equals(event.getType())) {
      handleActiveFilterChanged(event);
    }
    else {
      super.handleUiEvent(event);
    }
  }

  @Override
  protected void detachModel() {
    getJsonSession().currentJsonResponse().getEventList();
  }

  private void handleActiveFilterChanged(JsonEvent event) {
    String state = event.getData().optString("state");
    getModel().updateActiveFilter(TriState.valueOf(state));
  }

  @Override
  public JSONObject toJson() {
    JSONObject json = super.toJson();
    if (getModel().isActiveFilterEnabled()) {
      putProperty(json, "activeFilter", getModel().getActiveFilter().name());
    }
    return json;
  }
}
