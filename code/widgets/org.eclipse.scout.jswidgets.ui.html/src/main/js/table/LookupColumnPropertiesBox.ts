/*
 * Copyright (c) 2010, 2024 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
import {GroupBox, GroupBoxModel, InitModelOf, LookupColumn} from '@eclipse-scout/core';
import model, {LookupColumnPropertiesBoxWidgetMap} from './LookupColumnPropertiesBoxModel';

export class LookupColumnPropertiesBox extends GroupBox {
  declare widgetMap: LookupColumnPropertiesBoxWidgetMap;

  column: LookupColumn<any>;

  constructor() {
    super();
    this.column = null;
  }

  protected override _jsonModel(): GroupBoxModel {
    return model();
  }

  protected override _init(model: InitModelOf<this>) {
    super._init(model);

    this._setColumn(this.column);
  }

  setColumn(column: LookupColumn<any>) {
    this.setProperty('column', column);
  }

  protected _setColumn(column: LookupColumn<any>) {
    this._setProperty('column', column);
    if (!this.column) {
      return;
    }

    this.widget('LookupCallField').setValue(this.column.lookupCall);
    this.widget('LookupCallField').on('propertyChange:value', event => this.column.setLookupCall(event.newValue));

    this.widget('BrowseMaxRowCountField').setValue(this.column.browseMaxRowCount);
    this.widget('BrowseMaxRowCountField').on('propertyChange:value', event => this.column.setBrowseMaxRowCount(event.newValue));

    this.widget('BrowseHierarchyField').setValue(this.column.browseHierarchy);
    this.widget('BrowseHierarchyField').on('propertyChange:value', event => this.column.setBrowseHierarchy(event.newValue));

    this.widget('DistinctField').setValue(this.column.distinct);
    this.widget('DistinctField').on('propertyChange:value', event => this.column.setDistinct(event.newValue));
  }
}
