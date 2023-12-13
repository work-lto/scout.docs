/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.contacts.events.shared.person;

import java.util.Date;

import jakarta.annotation.Generated;

import org.eclipse.scout.contacts.shared.person.PersonFormData;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.extension.Extends;
import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.tablefield.AbstractTableFieldBeanData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Extends(PersonFormData.class)
@Generated(value = "org.eclipse.scout.contacts.events.client.person.PersonFormTabExtension", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class PersonFormTabExtensionData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public Events getEvents() {
    return getFieldByClass(Events.class);
  }

  @ClassId("0f91d144-2533-4d0f-976e-978b830e784f-formdata")
  public static class Events extends AbstractTableFieldBeanData {
    private static final long serialVersionUID = 1L;

    @Override
    public EventsRowData addRow() {
      return (EventsRowData) super.addRow();
    }

    @Override
    public EventsRowData addRow(int rowState) {
      return (EventsRowData) super.addRow(rowState);
    }

    @Override
    public EventsRowData createRow() {
      return new EventsRowData();
    }

    @Override
    public Class<? extends AbstractTableRowData> getRowType() {
      return EventsRowData.class;
    }

    @Override
    public EventsRowData[] getRows() {
      return (EventsRowData[]) super.getRows();
    }

    @Override
    public EventsRowData rowAt(int index) {
      return (EventsRowData) super.rowAt(index);
    }

    public void setRows(EventsRowData[] rows) {
      super.setRows(rows);
    }

    public static class EventsRowData extends AbstractTableRowData {
      private static final long serialVersionUID = 1L;
      public static final String id = "id";
      public static final String title = "title";
      public static final String starts = "starts";
      public static final String city = "city";
      public static final String country = "country";
      private String m_id;
      private String m_title;
      private Date m_starts;
      private String m_city;
      private String m_country;

      public String getId() {
        return m_id;
      }

      public void setId(String newId) {
        m_id = newId;
      }

      public String getTitle() {
        return m_title;
      }

      public void setTitle(String newTitle) {
        m_title = newTitle;
      }

      public Date getStarts() {
        return m_starts;
      }

      public void setStarts(Date newStarts) {
        m_starts = newStarts;
      }

      public String getCity() {
        return m_city;
      }

      public void setCity(String newCity) {
        m_city = newCity;
      }

      public String getCountry() {
        return m_country;
      }

      public void setCountry(String newCountry) {
        m_country = newCountry;
      }
    }
  }
}
