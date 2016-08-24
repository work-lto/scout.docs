/*******************************************************************************
 * Copyright (c) 2014-2015 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
scout.models = {

  modelMap: {},

  /**
   * @param modelsUrl relative URL points to the *-models.json file. Example: 'myproject-models.json'.
   */
  bootstrap: function(modelsUrl) {
    scout.objects.mandatoryParameter('modelsUrl', modelsUrl);
    var that = this;
    return $.ajax({
      url: modelsUrl,
      dataType: 'json',
      contentType: 'application/json; charset=UTF-8'
    }).done(that._onModelDone.bind(that))
      .fail(that._onModelFail.bind(that));
  },

  _onModelDone: function(data) {
    this.modelMap = data;
  },

  _onModelFail: function(jqXHR, textStatus, errorThrown) {
    throw new Error('Error while loading model: ' + errorThrown);
  },

  get: function(modelId, parent) {
    if (!parent) {
      throw new Error('missing argument parent');
    }
    var model = this.modelMap[modelId];
    model.parent = parent;
    return model;
  }

};
