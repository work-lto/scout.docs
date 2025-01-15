/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
import {Button, CheckBoxField, FormModel, GroupBox, NativeNotificationVisibility, NumberField, SmartField, StatusSeverity, StringField, TabBox, TabItem} from '@eclipse-scout/core';
import {EventsTab, EventsTabWidgetMap, IconIdLookupCall, ImageLookupCall, NativeNotificationVisibilityLookupCall, StatusSeverityLookupCall} from '../index';

export default (): FormModel => ({
  id: 'jswidgets.DesktopNotificationForm',
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
            id: 'Button',
            objectType: Button,
            label: 'Show Notification',
            processButton: false,
            gridDataHints: {
              horizontalAlignment: 0
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
                    id: 'MessageField',
                    objectType: StringField,
                    label: 'Message'
                  },
                  {
                    id: 'StatusSeverityField',
                    objectType: SmartField<StatusSeverity>,
                    lookupCall: StatusSeverityLookupCall,
                    label: 'Severity'
                  },
                  {
                    id: 'DurationField',
                    objectType: NumberField,
                    label: 'Duration',
                    tooltipText: '${textKey:DurationTooltip}'
                  },
                  {
                    id: 'IconField',
                    objectType: SmartField<string>,
                    lookupCall: IconIdLookupCall,
                    label: 'Icon'
                  },
                  {
                    id: 'ClosableField',
                    objectType: CheckBoxField,
                    label: 'Closable',
                    labelVisible: false
                  },
                  {
                    id: 'LoadingField',
                    objectType: CheckBoxField,
                    label: 'Loading',
                    labelVisible: false
                  },
                  {
                    id: 'HtmlEnabledField',
                    objectType: CheckBoxField,
                    label: 'HTML Enabled',
                    labelVisible: false
                  },
                  {
                    id: 'NativeOnlyField',
                    objectType: CheckBoxField,
                    label: 'Native Only',
                    labelVisible: false,
                    tooltipText: '${textKey:NativeOnlyTooltip}'
                  },
                  {
                    id: 'NativeNotificationTitleField',
                    objectType: StringField,
                    label: 'Native Notification Title',
                    labelWidthInPixel: 170
                  },
                  {
                    id: 'NativeNotificationMessageField',
                    objectType: StringField,
                    label: 'Native Notification Message',
                    labelWidthInPixel: 170
                  },
                  {
                    id: 'NativeNotificationIconIdField',
                    objectType: SmartField<string>,
                    lookupCall: ImageLookupCall,
                    label: 'Native Notification Icon Id',
                    labelWidthInPixel: 170
                  },
                  {
                    id: 'NativeNotificationVisibilityField',
                    objectType: SmartField<NativeNotificationVisibility>,
                    lookupCall: NativeNotificationVisibilityLookupCall,
                    label: 'Native Notification Visibility',
                    labelWidthInPixel: 170,
                    tooltipText: '${textKey:NativeNotificationVisibilityTooltip}'
                  },
                  {
                    id: 'DelayField',
                    objectType: NumberField,
                    label: 'Delay',
                    labelWidthInPixel: 170,
                    tooltipText: '${textKey:DelayTooltip}'
                  }
                ]
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

export type DesktopNotificationFormWidgetMap = {
  'MainBox': GroupBox;
  'DetailBox': GroupBox;
  'Button': Button;
  'ConfigurationBox': TabBox;
  'PropertiesTab': TabItem;
  'PropertiesBox': GroupBox;
  'MessageField': StringField;
  'StatusSeverityField': SmartField<StatusSeverity>;
  'DurationField': NumberField;
  'IconField': SmartField<string>;
  'ClosableField': CheckBoxField;
  'LoadingField': CheckBoxField;
  'HtmlEnabledField': CheckBoxField;
  'NativeOnlyField': CheckBoxField;
  'NativeNotificationTitleField': StringField;
  'NativeNotificationMessageField': StringField;
  'NativeNotificationIconIdField': SmartField<string>;
  'NativeNotificationVisibilityField': SmartField<NativeNotificationVisibility>;
  'DelayField': NumberField;
  'EventsTab': EventsTab;
} & EventsTabWidgetMap;
