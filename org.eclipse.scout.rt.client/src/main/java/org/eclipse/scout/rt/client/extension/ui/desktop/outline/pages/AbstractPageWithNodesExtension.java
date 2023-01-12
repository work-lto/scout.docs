/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.rt.client.extension.ui.desktop.outline.pages;

import java.util.List;

import org.eclipse.scout.rt.client.extension.ui.desktop.outline.pages.PageWithNodesChains.PageWithNodesCreateChildPagesChain;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;

public abstract class AbstractPageWithNodesExtension<OWNER extends AbstractPageWithNodes> extends AbstractPageExtension<OWNER> implements IPageWithNodesExtension<OWNER> {

  public AbstractPageWithNodesExtension(OWNER owner) {
    super(owner);
  }

  @Override
  public void execCreateChildPages(PageWithNodesCreateChildPagesChain chain, List<IPage<?>> pageList) {
    chain.execCreateChildPages(pageList);
  }
}
