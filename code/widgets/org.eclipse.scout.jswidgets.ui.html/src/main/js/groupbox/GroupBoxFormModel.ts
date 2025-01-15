/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
import {FormField, FormModel, GroupBox, Menu, SmartField, StringField, TabBox, TabItem} from '@eclipse-scout/core';
import {
  EventsTab, EventsTabWidgetMap, FormFieldActionsBox, FormFieldActionsBoxWidgetMap, FormFieldPropertiesBox, FormFieldPropertiesBoxWidgetMap, GridDataBox, GridDataBoxWidgetMap, GroupBoxAddFieldBox, GroupBoxAddFieldBoxWidgetMap,
  GroupBoxAddMenuBox, GroupBoxAddMenuBoxWidgetMap, GroupBoxDeleteFieldBox, GroupBoxDeleteFieldBoxWidgetMap, GroupBoxDeleteMenuBox, GroupBoxDeleteMenuBoxWidgetMap, GroupBoxPropertiesBox, GroupBoxPropertiesBoxWidgetMap,
  LogicalGridLayoutConfigBox, LogicalGridLayoutConfigBoxWidgetMap, WidgetActionsBox, WidgetActionsBoxWidgetMap
} from '../index';

export default (): FormModel => ({
  id: 'jswidgets.GroupBoxForm',
  displayHint: 'view',
  rootGroupBox: {
    id: 'MainBox',
    objectType: GroupBox,
    fields: [
      {
        id: 'DetailBox',
        objectType: GroupBox,
        label: 'Group Box',
        fields: [
          {
            id: 'StringField1',
            objectType: StringField,
            label: 'String Field 1'
          },
          {
            id: 'StringField2',
            objectType: StringField,
            label: 'String Field 2'
          },
          {
            id: 'StringField3',
            objectType: StringField,
            label: 'String Field 3'
          },
          {
            id: 'StringField4',
            objectType: StringField,
            label: 'String Field 4'
          }
        ],
        menus: [
          {
            id: 'Menu1',
            objectType: Menu,
            text: 'Menu',
            horizontalAlignment: 1
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
            label: 'Group Box Properties',
            fields: [
              {
                id: 'GroupBoxPropertiesBox',
                objectType: GroupBoxPropertiesBox,
                expandable: true,
                labelVisible: false,
                borderVisible: false
              },
              {
                id: 'FormFieldPropertiesBox',
                objectType: FormFieldPropertiesBox
              },
              {
                id: 'GridDataBox',
                objectType: GridDataBox,
                label: 'Grid Data Hints'
              },
              {
                id: 'BodyLayoutConfigBox',
                objectType: LogicalGridLayoutConfigBox
              }
            ]
          },
          {
            id: 'FieldPropertiesTab',
            objectType: TabItem,
            label: 'Field Properties',
            fields: [
              {
                id: 'Field.TargetField',
                objectType: SmartField<FormField>,
                label: 'Target'
              },
              {
                id: 'Field.FormFieldPropertiesBox',
                objectType: FormFieldPropertiesBox
              },
              {
                id: 'Field.GridDataBox',
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
                id: 'Actions.AddMenuBox',
                objectType: GroupBoxAddMenuBox
              },
              {
                id: 'Actions.DeleteMenuBox',
                objectType: GroupBoxDeleteMenuBox
              },
              {
                id: 'Actions.AddFieldBox',
                objectType: GroupBoxAddFieldBox
              },
              {
                id: 'Actions.DeleteFieldBox',
                objectType: GroupBoxDeleteFieldBox
              },
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

export type GroupBoxFormWidgetMap =
  {
    'MainBox': GroupBox;
    'DetailBox': GroupBox;
    'StringField1': StringField;
    'StringField2': StringField;
    'StringField3': StringField;
    'StringField4': StringField;
    'Menu1': Menu;
    'ConfigurationBox': TabBox;
    'PropertiesTab': TabItem;
    'GroupBoxPropertiesBox': GroupBoxPropertiesBox;
    'FormFieldPropertiesBox': FormFieldPropertiesBox;
    'GridDataBox': GridDataBox;
    'BodyLayoutConfigBox': LogicalGridLayoutConfigBox;
    'FieldPropertiesTab': TabItem;
    'Field.TargetField': SmartField<FormField>;
    'Field.FormFieldPropertiesBox': FormFieldPropertiesBox;
    'Field.GridDataBox': GridDataBox;
    'ActionsTab': TabItem;
    'Actions.AddMenuBox': GroupBoxAddMenuBox;
    'Actions.DeleteMenuBox': GroupBoxDeleteMenuBox;
    'Actions.AddFieldBox': GroupBoxAddFieldBox;
    'Actions.DeleteFieldBox': GroupBoxDeleteFieldBox;
    'FormFieldActionsBox': FormFieldActionsBox;
    'WidgetActionsBox': WidgetActionsBox;
    'EventsTab': EventsTab;
  }
  & GroupBoxPropertiesBoxWidgetMap
  & FormFieldPropertiesBoxWidgetMap
  & GridDataBoxWidgetMap
  & LogicalGridLayoutConfigBoxWidgetMap
  & GroupBoxAddMenuBoxWidgetMap
  & GroupBoxDeleteMenuBoxWidgetMap
  & GroupBoxAddFieldBoxWidgetMap
  & GroupBoxDeleteFieldBoxWidgetMap
  & FormFieldActionsBoxWidgetMap
  & WidgetActionsBoxWidgetMap
  & EventsTabWidgetMap;
