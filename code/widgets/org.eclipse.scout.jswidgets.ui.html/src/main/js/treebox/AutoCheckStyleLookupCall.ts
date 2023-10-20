/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
import {AutoCheckStyle, StaticLookupCall, Tree} from '@eclipse-scout/core';

export class AutoCheckStyleLookupCall extends StaticLookupCall<AutoCheckStyle> {

  protected override _data(): any[] {
    return AutoCheckStyleLookupCall.DATA;
  }

  static DATA = [
    [Tree.AutoCheckStyle.NONE, 'NONE'],
    [Tree.AutoCheckStyle.AUTO_CHECK_CHILD_NODES, 'AUTO_CHECK_CHILD_NODES'],
    [Tree.AutoCheckStyle.SYNC_CHILD_AND_PARENT_STATE, 'SYNC_CHILD_AND_PARENT_STATE']
  ];
}
