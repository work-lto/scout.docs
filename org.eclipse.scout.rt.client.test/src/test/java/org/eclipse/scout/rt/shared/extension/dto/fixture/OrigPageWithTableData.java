/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.rt.shared.extension.dto.fixture;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "org.eclipse.scout.rt.shared.extension.dto.fixture.OrigPageWithTable", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class OrigPageWithTableData extends AbstractTablePageData {
  private static final long serialVersionUID = 1L;

  @Override
  public OrigPageWithTableRowData addRow() {
    return (OrigPageWithTableRowData) super.addRow();
  }

  @Override
  public OrigPageWithTableRowData addRow(int rowState) {
    return (OrigPageWithTableRowData) super.addRow(rowState);
  }

  @Override
  public OrigPageWithTableRowData createRow() {
    return new OrigPageWithTableRowData();
  }

  @Override
  public Class<? extends AbstractTableRowData> getRowType() {
    return OrigPageWithTableRowData.class;
  }

  @Override
  public OrigPageWithTableRowData[] getRows() {
    return (OrigPageWithTableRowData[]) super.getRows();
  }

  @Override
  public OrigPageWithTableRowData rowAt(int index) {
    return (OrigPageWithTableRowData) super.rowAt(index);
  }

  public void setRows(OrigPageWithTableRowData[] rows) {
    super.setRows(rows);
  }

  public static class OrigPageWithTableRowData extends AbstractTableRowData {
    private static final long serialVersionUID = 1L;
    public static final String firstBigDecimal = "firstBigDecimal";
    public static final String secondSmart = "secondSmart";
    private BigDecimal m_firstBigDecimal;
    private Long m_secondSmart;

    public BigDecimal getFirstBigDecimal() {
      return m_firstBigDecimal;
    }

    public void setFirstBigDecimal(BigDecimal newFirstBigDecimal) {
      m_firstBigDecimal = newFirstBigDecimal;
    }

    public Long getSecondSmart() {
      return m_secondSmart;
    }

    public void setSecondSmart(Long newSecondSmart) {
      m_secondSmart = newSecondSmart;
    }
  }
}
