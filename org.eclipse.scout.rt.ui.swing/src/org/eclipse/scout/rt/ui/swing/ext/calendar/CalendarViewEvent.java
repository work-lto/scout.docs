/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.rt.ui.swing.ext.calendar;

import java.util.EventObject;

public class CalendarViewEvent extends EventObject {
  private static final long serialVersionUID = 1L;

  /**
   * viewDateStart, viewDateEnd
   */
  public static final int TYPE_VISIBLE_RANGE_CHANGED = 10;

  /**
   * displayMode, displayCondensed
   */
  public static final int TYPE_SETUP_CHANGED = 20;

  /**
   * selectedDate, selectedItem
   */
  public static final int TYPE_SELECTION_CHANGED = 30;

  private int m_type;

  public CalendarViewEvent(EventObject source, int type) {
    super(source);
    m_type = type;
  }

  public CalendarViewEvent(Object source, int type) {
    super(source);
    m_type = type;
  }

  @Override
  public DateChooser getSource() {
    return (DateChooser) super.getSource();
  }

  public int getType() {
    return m_type;
  }

}
