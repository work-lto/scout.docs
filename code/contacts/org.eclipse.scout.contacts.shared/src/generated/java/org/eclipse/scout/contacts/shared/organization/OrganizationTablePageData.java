/*
 * Copyright (c) 2010, 2024 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.contacts.shared.organization;

import jakarta.annotation.Generated;

import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@ClassId("18f7a78e-0dd0-4e4e-9234-99892bb4459f-formdata")
@Generated(value = "org.eclipse.scout.contacts.client.organization.OrganizationTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class OrganizationTablePageData extends AbstractTablePageData {
  private static final long serialVersionUID = 1L;

  @Override
  public OrganizationTableRowData addRow() {
    return (OrganizationTableRowData) super.addRow();
  }

  @Override
  public OrganizationTableRowData addRow(int rowState) {
    return (OrganizationTableRowData) super.addRow(rowState);
  }

  @Override
  public OrganizationTableRowData createRow() {
    return new OrganizationTableRowData();
  }

  @Override
  public Class<? extends AbstractTableRowData> getRowType() {
    return OrganizationTableRowData.class;
  }

  @Override
  public OrganizationTableRowData[] getRows() {
    return (OrganizationTableRowData[]) super.getRows();
  }

  @Override
  public OrganizationTableRowData rowAt(int index) {
    return (OrganizationTableRowData) super.rowAt(index);
  }

  public void setRows(OrganizationTableRowData[] rows) {
    super.setRows(rows);
  }

  public static class OrganizationTableRowData extends AbstractTableRowData {
    private static final long serialVersionUID = 1L;
    public static final String organizationId = "organizationId";
    public static final String name = "name";
    public static final String city = "city";
    public static final String country = "country";
    public static final String homepage = "homepage";
    private String m_organizationId;
    private String m_name;
    private String m_city;
    private String m_country;
    private String m_homepage;

    public String getOrganizationId() {
      return m_organizationId;
    }

    public void setOrganizationId(String newOrganizationId) {
      m_organizationId = newOrganizationId;
    }

    public String getName() {
      return m_name;
    }

    public void setName(String newName) {
      m_name = newName;
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

    public String getHomepage() {
      return m_homepage;
    }

    public void setHomepage(String newHomepage) {
      m_homepage = newHomepage;
    }
  }
}
