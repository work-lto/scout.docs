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
package org.eclipse.scout.commons.holders;

import java.util.Date;

/**
 * @since 3.0
 */

public class DateHolder extends Holder<Date> {
  private static final long serialVersionUID = 1L;

  public DateHolder() {
    super(Date.class);
  }

  public DateHolder(Date value) {
    super(Date.class, value);
  }

}
