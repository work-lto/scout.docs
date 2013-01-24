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
package org.eclipse.scout.rt.ui.swing.ext;

import java.awt.Window;

import javax.swing.JRootPane;
import javax.swing.JWindow;

/**
 * JDialog with support for extended root frame, see {@link JRootPaneEx}
 */
public class JWindowEx extends JWindow {
  private static final long serialVersionUID = 1L;

  public JWindowEx() {
    super();
  }

  public JWindowEx(Window w) {
    super(w);
  }

  @Override
  protected JRootPane createRootPane() {
    return new JRootPaneEx() {
      private static final long serialVersionUID = 1L;
    };
  }

}
