/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
import {Carousel, CarouselField, CheckBoxField, DateField, Form, FormModel, GroupBox, Image, StringField, TabBox, TabItem} from '@eclipse-scout/core';
import {
  EventsTab, EventsTabWidgetMap, FormFieldActionsBox, FormFieldActionsBoxWidgetMap, FormFieldPropertiesBox, FormFieldPropertiesBoxWidgetMap, GridDataBox, GridDataBoxWidgetMap, WidgetActionsBox, WidgetActionsBoxWidgetMap
} from '../index';

export default (): FormModel => ({
  id: 'jswidgets.CarouselForm',
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
            id: 'CarouselField',
            objectType: CarouselField,
            labelVisible: false,
            gridDataHints: {
              h: 5
            },
            carousel: {
              id: 'Carousel',
              objectType: Carousel,
              widgets: [
                {
                  id: 'CarouselItem1',
                  objectType: Image,
                  imageUrl: 'img/bird.jpg',
                  autoFit: true
                },
                {
                  id: 'CarouselItem2',
                  objectType: Form,
                  displayHint: 'view',
                  rootGroupBox: {
                    id: 'InnerMainBox1',
                    objectType: GroupBox,
                    fields: [
                      {
                        objectType: StringField,
                        label: 'Field 1'
                      },
                      {
                        objectType: StringField,
                        label: 'Field 2'
                      }
                    ]
                  }
                },
                {
                  id: 'CarouselItem3',
                  objectType: Image,
                  imageUrl: 'img/eclipse_scout_logo.png',
                  cssClass: 'scout-image'
                },
                {
                  id: 'CarouselItem4',
                  objectType: Form,
                  displayHint: 'view',
                  rootGroupBox: {
                    id: 'InnerMainBox2',
                    objectType: GroupBox,
                    fields: [
                      {
                        objectType: DateField,
                        label: 'Date field 1'
                      },
                      {
                        objectType: DateField,
                        label: 'Date field 1'
                      },
                      {
                        objectType: DateField,
                        label: 'Date field 3'
                      },
                      {
                        objectType: DateField,
                        label: 'Date field 4'
                      }
                    ]
                  }
                }
              ]
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
                    id: 'StatusEnabledField',
                    objectType: CheckBoxField,
                    labelVisible: false,
                    label: 'Status enabled'
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
                label: 'Grid Data Hints',
                expanded: false
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

export type CarouselFormWidgetMap = {
  'MainBox': GroupBox;
  'DetailBox': GroupBox;
  'CarouselField': CarouselField;
  'Carousel': Carousel;
  'CarouselItem1': Image;
  'CarouselItem2': Form;
  'InnerMainBox1': GroupBox;
  'CarouselItem3': Image;
  'CarouselItem4': Form;
  'InnerMainBox2': GroupBox;
  'ConfigurationBox': TabBox;
  'PropertiesTab': TabItem;
  'PropertiesBox': GroupBox;
  'StatusEnabledField': CheckBoxField;
  'FormFieldPropertiesBox': FormFieldPropertiesBox;
  'GridDataBox': GridDataBox;
  'ActionsTab': TabItem;
  'FormFieldActionsBox': FormFieldActionsBox;
  'WidgetActionsBox': WidgetActionsBox;
  'EventsTab': EventsTab;
} & FormFieldPropertiesBoxWidgetMap & GridDataBoxWidgetMap & FormFieldActionsBoxWidgetMap & WidgetActionsBoxWidgetMap & EventsTabWidgetMap;
