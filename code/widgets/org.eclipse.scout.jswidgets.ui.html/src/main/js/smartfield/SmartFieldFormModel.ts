/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
import {FormModel, GroupBox, Menu, SmartField, StatusMenuMapping, TabBox, TabItem} from '@eclipse-scout/core';
import {
  EventsTab, EventsTabWidgetMap, FormFieldActionsBox, FormFieldActionsBoxWidgetMap, FormFieldPropertiesBox, FormFieldPropertiesBoxWidgetMap, GridDataBox, GridDataBoxWidgetMap, LocaleLookupCall, SmartFieldPropertiesBox,
  SmartFieldPropertiesBoxWidgetMap, ValueFieldPropertiesBox, ValueFieldPropertiesBoxWidgetMap, WidgetActionsBox, WidgetActionsBoxWidgetMap
} from '../index';

export default (): FormModel => ({
  id: 'jswidgets.SmartFieldForm',
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
            id: 'SmartField',
            objectType: SmartField,
            lookupCall: LocaleLookupCall,
            label: 'Smart Field',
            menus: [
              {
                id: 'NewLanguageMenu',
                text: '${textKey:NewLanguageMenu}',
                objectType: Menu
              }
            ],
            statusMenuMappings: [
              {
                id: 'NewLanguageMenuStatusMapping',
                objectType: StatusMenuMapping,
                menu: 'NewLanguageMenu',
                codes: [
                  2
                ]
              }
            ]
          }
        ]
      },
      {
        id: 'ConfigurationBox',
        objectType: TabBox,
        cssClass: 'jswidgets-configuration',
        gridColumnCount: 1,
        selectedTab: 'PropertiesTab',
        tabItems: [
          {
            id: 'PropertiesTab',
            objectType: TabItem,
            label: 'Properties',
            fields: [
              {
                id: 'SmartFieldPropertiesBox',
                objectType: SmartFieldPropertiesBox,
                label: 'Smart Field Properties',
                labelVisible: false,
                borderVisible: false
              },
              {
                id: 'ValueFieldPropertiesBox',
                objectType: ValueFieldPropertiesBox
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

export type SmartFieldFormWidgetMap =
  {
    'MainBox': GroupBox;
    'DetailBox': GroupBox;
    'SmartField': SmartField<any>;
    'NewLanguageMenu': Menu;
    'NewLanguageMenuStatusMapping': StatusMenuMapping;
    'ConfigurationBox': TabBox;
    'PropertiesTab': TabItem;
    'SmartFieldPropertiesBox': SmartFieldPropertiesBox;
    'ValueFieldPropertiesBox': ValueFieldPropertiesBox;
    'FormFieldPropertiesBox': FormFieldPropertiesBox;
    'GridDataBox': GridDataBox;
    'ActionsTab': TabItem;
    'FormFieldActionsBox': FormFieldActionsBox;
    'WidgetActionsBox': WidgetActionsBox;
    'EventsTab': EventsTab;
  }
  & SmartFieldPropertiesBoxWidgetMap & ValueFieldPropertiesBoxWidgetMap & FormFieldPropertiesBoxWidgetMap & GridDataBoxWidgetMap
  & FormFieldActionsBoxWidgetMap & WidgetActionsBoxWidgetMap & EventsTabWidgetMap;
