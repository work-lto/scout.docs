/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.rt.client.extension.ui.basic.table.columns;

import java.math.BigInteger;

import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigIntegerColumn;

public abstract class AbstractBigIntegerColumnExtension<OWNER extends AbstractBigIntegerColumn> extends AbstractNumberColumnExtension<BigInteger, OWNER> implements IBigIntegerColumnExtension<OWNER> {

  public AbstractBigIntegerColumnExtension(OWNER owner) {
    super(owner);
  }
}
