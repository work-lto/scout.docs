/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.rt.client.extension.ui.form.fields.listbox;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.extension.ui.form.fields.AbstractValueFieldExtension;
import org.eclipse.scout.rt.client.extension.ui.form.fields.listbox.ListBoxChains.ListBoxFilterLookupResultChain;
import org.eclipse.scout.rt.client.extension.ui.form.fields.listbox.ListBoxChains.ListBoxLoadTableDataChain;
import org.eclipse.scout.rt.client.extension.ui.form.fields.listbox.ListBoxChains.ListBoxPopulateTableChain;
import org.eclipse.scout.rt.client.extension.ui.form.fields.listbox.ListBoxChains.ListBoxPrepareLookupChain;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;

public abstract class AbstractListBoxExtension<KEY, OWNER extends AbstractListBox<KEY>> extends AbstractValueFieldExtension<Set<KEY>, OWNER> implements IListBoxExtension<KEY, OWNER> {

  public AbstractListBoxExtension(OWNER owner) {
    super(owner);
  }

  @Override
  public void execPopulateTable(ListBoxPopulateTableChain<KEY> chain) {
    chain.execPopulateTable();
  }

  @Override
  public List<? extends ILookupRow<KEY>> execLoadTableData(ListBoxLoadTableDataChain<KEY> chain) {
    return chain.execLoadTableData();
  }

  @Override
  public void execFilterLookupResult(ListBoxFilterLookupResultChain<KEY> chain, ILookupCall<KEY> call, List<ILookupRow<KEY>> result) {
    chain.execFilterLookupResult(call, result);
  }

  @Override
  public void execPrepareLookup(ListBoxPrepareLookupChain<KEY> chain, ILookupCall<KEY> call) {
    chain.execPrepareLookup(call);
  }
}
