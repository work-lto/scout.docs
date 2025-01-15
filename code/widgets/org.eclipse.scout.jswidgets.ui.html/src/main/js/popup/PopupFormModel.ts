/*
 * Copyright (c) 2010, 2024 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
import {Button, CheckBoxField, FormModel, GroupBox, PopupAlignment, SmartField, StringField, TabItem} from '@eclipse-scout/core';
import {
  ConfigurationBox, EventsTab, EventsTabWidgetMap, PopupHorizontalAlignLookupCall, PopupVerticalAlignLookupCall, WidgetActionsBox, WidgetActionsBoxWidgetMap, WidgetPopupPropertiesBox, WidgetPopupPropertiesBoxWidgetMap
} from '../index';

export default (): FormModel => ({
  id: 'jswidgets.PopupForm',
  displayHint: 'view',
  rootGroupBox: {
    id: 'MainBox',
    objectType: GroupBox,
    fields: [
      {
        id: 'DetailBox',
        objectType: GroupBox,
        gridColumnCount: 1,
        gridDataHints: {
          weightY: 1
        },
        responsive: false,
        fields: [
          {
            id: 'OpenPopupButton',
            objectType: Button,
            label: '${textKey:OpenPopup}',
            cssClass: 'open-form-button',
            processButton: false,
            gridDataHints: {
              horizontalAlignment: 0,
              verticalAlignment: 0,
              fillVertical: false,
              fillHorizontal: false,
              weightY: 1
            }
          }
        ]
      },
      {
        id: 'ConfigurationBox',
        objectType: ConfigurationBox,
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
                    id: 'UseButtonAsAnchorField',
                    objectType: CheckBoxField,
                    label: 'Use button as anchor',
                    labelVisible: false
                  },
                  {
                    id: 'AnchorBoundsField',
                    objectType: StringField,
                    label: 'Anchor Bounds',
                    tooltipText: '${textKey:AnchorBoundsTooltip}'
                  },
                  {
                    id: 'ModalField',
                    objectType: CheckBoxField,
                    label: 'Modal',
                    tooltipText: 'If set, the following properties are overruled: "closeOnAnchorMouseDown", "closeOnMouseDownOutside", "closeOnOtherPopupOpen" and "withGlassPane".',
                    labelVisible: false
                  },
                  {
                    id: 'CloseOnAnchorMouseDownField',
                    objectType: CheckBoxField,
                    label: 'Close on Anchor Mouse Down',
                    labelVisible: false
                  },
                  {
                    id: 'CloseOnMouseDownOutsideField',
                    objectType: CheckBoxField,
                    label: 'Close on Mouse Down Outside',
                    labelVisible: false
                  },
                  {
                    id: 'CloseOnOtherPopupOpenField',
                    objectType: CheckBoxField,
                    label: 'Close on Other Popup Open',
                    labelVisible: false
                  },
                  {
                    id: 'HorizontalSwitchField',
                    objectType: CheckBoxField,
                    label: 'Horizontal Switch',
                    labelVisible: false
                  },
                  {
                    id: 'VerticalSwitchField',
                    objectType: CheckBoxField,
                    label: 'Vertical Switch',
                    labelVisible: false
                  },
                  {
                    id: 'TrimWidthField',
                    objectType: CheckBoxField,
                    label: 'Trim Width',
                    labelVisible: false
                  },
                  {
                    id: 'TrimHeightField',
                    objectType: CheckBoxField,
                    label: 'Trim Height',
                    labelVisible: false
                  },
                  {
                    id: 'WithArrowField',
                    objectType: CheckBoxField,
                    label: 'With Arrow',
                    labelVisible: false
                  },
                  {
                    id: 'WithGlassPaneField',
                    objectType: CheckBoxField,
                    label: 'With Glass Pane',
                    labelVisible: false
                  },
                  {
                    id: 'HorizontalAlignmentField',
                    objectType: SmartField<PopupAlignment>,
                    lookupCall: PopupHorizontalAlignLookupCall,
                    label: 'Horizontal Alignment'
                  },
                  {
                    id: 'VerticalAlignmentField',
                    objectType: SmartField<PopupAlignment>,
                    lookupCall: PopupVerticalAlignLookupCall,
                    label: 'Vertical Alignment'
                  }
                ]
              },
              {
                id: 'WidgetPopupPropertiesBox',
                objectType: WidgetPopupPropertiesBox
              }
            ]
          },
          {
            id: 'ActionsTab',
            objectType: TabItem,
            label: 'Actions',
            fields: [
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

export type PopupFormWidgetMap = {
  'MainBox': GroupBox;
  'DetailBox': GroupBox;
  'OpenPopupButton': Button;
  'ConfigurationBox': ConfigurationBox;
  'PropertiesTab': TabItem;
  'PropertiesBox': GroupBox;
  'UseButtonAsAnchorField': CheckBoxField;
  'AnchorBoundsField': StringField;
  'ModalField': CheckBoxField;
  'CloseOnAnchorMouseDownField': CheckBoxField;
  'CloseOnMouseDownOutsideField': CheckBoxField;
  'CloseOnOtherPopupOpenField': CheckBoxField;
  'HorizontalSwitchField': CheckBoxField;
  'VerticalSwitchField': CheckBoxField;
  'TrimWidthField': CheckBoxField;
  'TrimHeightField': CheckBoxField;
  'WithArrowField': CheckBoxField;
  'WithGlassPaneField': CheckBoxField;
  'HorizontalAlignmentField': SmartField<PopupAlignment>;
  'VerticalAlignmentField': SmartField<PopupAlignment>;
  'WidgetPopupPropertiesBox': WidgetPopupPropertiesBox;
  'ActionsTab': TabItem;
  'WidgetActionsBox': WidgetActionsBox;
  'EventsTab': EventsTab;
} & WidgetPopupPropertiesBoxWidgetMap & WidgetActionsBoxWidgetMap & EventsTabWidgetMap;
