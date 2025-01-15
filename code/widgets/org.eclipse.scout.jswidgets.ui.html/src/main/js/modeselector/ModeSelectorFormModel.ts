/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
import {CheckBoxField, FormModel, GroupBox, Mode, ModeSelector, ModeSelectorField, SmartField, StringField, TabBox, TabItem} from '@eclipse-scout/core';
import {
  EventsTab, EventsTabWidgetMap, FormFieldActionsBox, FormFieldActionsBoxWidgetMap, FormFieldPropertiesBox, FormFieldPropertiesBoxWidgetMap, GridDataBox, GridDataBoxWidgetMap, IconIdLookupCall, WidgetActionsBox, WidgetActionsBoxWidgetMap
} from '../index';

export default (): FormModel => ({
  id: 'jswidgets.ModeSelectorForm',
  displayHint: 'view',
  rootGroupBox: {
    id: 'MainBox',
    objectType: GroupBox,
    fields: [
      {
        id: 'DetailBox',
        objectType: GroupBox,
        gridColumnCount: 1,
        fields: [
          {
            id: 'ModeSelectorField',
            objectType: ModeSelectorField,
            labelVisible: false,
            statusVisible: false,
            modeSelector: {
              id: 'ModeSelector',
              objectType: ModeSelector,
              selectedMode: 'Mode1',
              modes: [{
                id: 'Mode1',
                objectType: Mode,
                text: 'Mode 1'
              }, {
                id: 'Mode2',
                objectType: Mode,
                text: 'Mode 2'
              }, {
                id: 'Mode3',
                objectType: Mode,
                text: 'Mode 3'
              }]
            }
          }
        ]
      },
      {
        id: 'ConfigurationBox',
        objectType: TabBox,
        cssClass: 'jswidgets-configuration',
        selectedTab: 'PropertiesTab',
        tabItems: [
          {
            id: 'PropertiesTab',
            objectType: TabItem,
            label: 'Properties',
            fields: [
              {
                id: 'PropertiesBox',
                objectType: GroupBox,
                label: 'Properties',
                labelVisible: false,
                borderVisible: false,
                fields: [
                  {
                    id: 'TargetField',
                    objectType: SmartField<Mode>,
                    label: 'Target'
                  },
                  {
                    id: 'TextField',
                    objectType: StringField,
                    label: 'Text'
                  },
                  {
                    id: 'IconIdField',
                    objectType: SmartField<string>,
                    lookupCall: IconIdLookupCall,
                    label: 'Icon Id'
                  },
                  {
                    id: 'SelectedField',
                    objectType: CheckBoxField,
                    label: 'Selected',
                    labelVisible: false
                  },
                  {
                    id: 'EnabledField',
                    objectType: CheckBoxField,
                    label: 'Enabled',
                    labelVisible: false
                  },
                  {
                    id: 'VisibleField',
                    objectType: CheckBoxField,
                    label: 'Visible',
                    labelVisible: false
                  }
                ]
              },
              {
                id: 'FormFieldPropertiesBox',
                objectType: FormFieldPropertiesBox
              },
              {
                id: 'GridDataBox',
                objectType: GridDataBox,
                label: 'Grid Data Hints'
              }
            ]
          },
          {
            id: 'ActionsTab',
            objectType: TabItem,
            label: 'Actions',
            fields: [
              {
                id: 'FormFieldActionsBox',
                objectType: FormFieldActionsBox
              },
              {
                id: 'WidgetActionsBox',
                objectType: WidgetActionsBox
              }
            ]
          },
          {
            id: 'EventsTab',
            objectType: EventsTab
          }
        ]
      }
    ]
  }
});

/* **************************************************************************
* GENERATED WIDGET MAPS
* **************************************************************************/

export type ModeSelectorFormWidgetMap = {
  'MainBox': GroupBox;
  'DetailBox': GroupBox;
  'ModeSelectorField': ModeSelectorField<any>;
  'ModeSelector': ModeSelector;
  'Mode1': Mode;
  'Mode2': Mode;
  'Mode3': Mode;
  'ConfigurationBox': TabBox;
  'PropertiesTab': TabItem;
  'PropertiesBox': GroupBox;
  'TargetField': SmartField<Mode>;
  'TextField': StringField;
  'IconIdField': SmartField<string>;
  'SelectedField': CheckBoxField;
  'EnabledField': CheckBoxField;
  'VisibleField': CheckBoxField;
  'FormFieldPropertiesBox': FormFieldPropertiesBox;
  'GridDataBox': GridDataBox;
  'ActionsTab': TabItem;
  'FormFieldActionsBox': FormFieldActionsBox;
  'WidgetActionsBox': WidgetActionsBox;
  'EventsTab': EventsTab;
} & FormFieldPropertiesBoxWidgetMap & GridDataBoxWidgetMap & FormFieldActionsBoxWidgetMap & WidgetActionsBoxWidgetMap & EventsTabWidgetMap;
