/**
 * The MenuBarPopup is a special Popup that is used in the menu-bar. It is tightly coupled with a menu-item and shows a header
 * which has a different size than the popup-body.
 */
scout.MenuBarPopup = function(menu, session, event) {
  scout.MenuBarPopup.parent.call(this, session);
  this.menu = menu;
  this.$headBlueprint = this.menu.$container;
  this.ignoreEvent = event;
};
scout.inherits(scout.MenuBarPopup, scout.ContextMenuPopup);

/**
 * @override
 */
scout.MenuBarPopup.prototype._getMenuItems = function() {
  return this.menu.childActions || this.menu.menus;
};

/**
 * @override
 */
scout.MenuBarPopup.prototype._beforeRenderMenuItems = function() {
};

/**
 * @override popup
 * @param event
 */
scout.MenuBarPopup.prototype.close = function(event){
  if(!event || !this.ignoreEvent || event.originalEvent !== this.ignoreEvent.originalEvent){
    scout.MenuBarPopup.parent.prototype.close.call(this);
  }
};


/**
 * @override
 */
scout.MenuBarPopup.prototype._afterRenderMenuItems = function() {
  this.alignTo();
};

scout.MenuBarPopup.prototype._renderHead = function() {
  scout.MenuBarPopup.parent.prototype._renderHead.call(this);

  // FIXME AWE throws exception if this.menu is a button because button is not rendered (MenuButtonAdapter is)
  if (this.menu.$container.parent().hasClass('main-menubar')) {
    this.$head.addClass('in-main-menubar');
  }

  if (scout.Action.ActionStyle.TASK_BAR === this.menu.actionStyle) {
    this._copyCssClassToHead('taskbar-tool-item');
    this.$head.addClass('selected');
  } else {
    this._copyCssClassToHead('button');
    this._copyCssClassToHead('menu-textandicon');
  }
  this._copyCssClassToHead('has-submenu');
};

scout.MenuBarPopup.prototype.onMenuItemClicked = function(menu) {
  this.close();
  menu.sendDoAction();
};

scout.MenuBarPopup.prototype.alignTo = function() {
  var pos = this.menu.$container.offset(),
    headSize = scout.graphics.getSize(this.$head, true),
    bodyWidth = scout.graphics.getSize(this.$body, true).width;

  // body min-width
  if (bodyWidth < headSize.width) {
    this.$body.width(headSize.width - 2);
    bodyWidth = headSize.width;
  }

  // horiz. alignment
  var left = pos.left,
    top = pos.top - 6,
    headInsets = scout.graphics.getInsets(this.$head),
    bodyTop = headSize.height;

  $.log.debug('bodyWidth=' + bodyWidth + ' pos=[left' + pos.left + ' top=' + pos.top + '] headSize=' + headSize +
    ' headInsets=' + headInsets + ' left=' + left + ' top=' + top);
  this.$body.cssTop(bodyTop);
  this.$deco.cssTop(bodyTop);

  if (this.menu.$container.hasClass('right-aligned')) {
    // when we use float:right, browser uses fractions of pixels, that's why we must
    // use the subPixelCorr variable. It corrects some visual pixel-shifting issues.
    var widthDiff = bodyWidth - headSize.width,
      subPixelCorr = left - Math.floor(left);
    left -= widthDiff + headInsets.left;
    this.$head.cssLeft(widthDiff + 1);
    this.$body.cssLeft(subPixelCorr + 1);
    this.$deco.cssLeft(widthDiff + 2).width(headSize.width - 2 + subPixelCorr);
    $.log.debug('right alignment: widthDiff=' + widthDiff + ' subPixelCorr=' + subPixelCorr);
  } else if (this.menu.$container.hasClass('taskbar-tool-item')) {
    top = pos.top;
    bodyTop = headSize.height - 1;
    this.$body.cssTop(bodyTop);
    this.$deco.cssTop(bodyTop);
    this.$head.cssLeft(0);
    this.$deco.cssLeft(1).width(headSize.width - 2);
  } else {
    left -= headInsets.left;
    this.$head.cssLeft(0);
    this.$deco.cssLeft(1).width(headSize.width - 2);
  }

  this.setLocation(new scout.Point(left, top));
};
