/*
 * Copyright (c) 2014-2018 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 */
import {FormFieldTile, TableField} from '../../../index';
import * as $ from 'jquery';

export default class TileTableField extends TableField {

  constructor() {
    super();

    this._tableBlurHandler = this._onTableBlur.bind(this);
    this._tableFocusHandler = this._onTableFocus.bind(this);
    this._menuBarPropertyChangeHandler = this._onMenuBarPropertyChange.bind(this);
  }


  /**
   * @override
   */
  _renderTable() {
    super._renderTable();
    if (this.parent.displayStyle !== FormFieldTile.DisplayStyle.DASHBOARD) {
      return;
    }
    if (this.table) {
      // Never show header menus for this widget
      this.table.setHeaderMenusEnabled(false);
      // Disable sorting for user but don't disabled sorting in Java because CoreTableTile needs sorting functionality.
      this.table.setSortEnabled(false);
      this.table.$container
        .on('blur', this._tableBlurHandler)
        .on('focus', this._tableFocusHandler);
      this.table.menuBar.on('propertyChange', this._menuBarPropertyChangeHandler);
      this._toggleHasMenuBar();
      if (document.activeElement !== this.table.$container[0]) {
        this._hideMenuBar(true);
      }
    }
  }

  /**
   * @override
   */
  _removeTable() {
    if (this.parent.displayStyle !== FormFieldTile.DisplayStyle.DASHBOARD) {
      return;
    }
    if (this.table) {
      this.table.$container
        .off('blur', this._tableBlurHandler)
        .off('focus', this._tableFocusHandler);
      this.table.menuBar.off('propertyChange', this._menuBarPropertyChangeHandler);
    }
    super._removeTable();
  }

  _onTableBlur(event) {
    var popup = $('.popup').data('widget');

    // hide menu bar if context menu popup is not attached to TileTableField
    if (!this.has(popup)) {
      this._hideMenuBar(true);
    }
  }

  _onTableFocus(event) {
    this._hideMenuBar(false);
  }

  _hideMenuBar(hiddenByUi) {
    this.table.menuBar.hiddenByUi = hiddenByUi;
    this.table.menuBar.updateVisibility();
  }

  _onMenuBarPropertyChange(event) {
    if (event.propertyName === 'visible') {
      this._toggleHasMenuBar();
    }
  }

  _toggleHasMenuBar() {
    // adjust menu bar on TileTableField with the additional class has-menubar.
    this.$container.toggleClass('has-menubar', this.table.menuBar.visible);
  }
}
