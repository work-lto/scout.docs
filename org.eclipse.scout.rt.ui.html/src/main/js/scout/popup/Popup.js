scout.Popup = function(session, options) {
  scout.Popup.parent.call(this);
  options = options || {};
  this._mouseDownHandler;
  this.session = session;
  this.keyStrokeAdapter = this._createKeyStrokeAdapter();
  this.anchorBounds = options.anchorBounds;
  this.$anchor = options.$anchor;
  this.windowPaddingX = options.windowPaddingX !== undefined ? options.windowPaddingX : 10;
  this.windowPaddingY = options.windowPaddingY !== undefined ? options.windowPaddingY : 5;
  this.installFocusContext = options.installFocusContext !== undefined ? options.installFocusContext : true;
};
scout.inherits(scout.Popup, scout.Widget);

/**
 * The popup is always appended to the HTML document body.
 * That way we never have z-index issues with the rendered popups.
 */
scout.Popup.prototype.render = function($parent) {
  scout.Popup.parent.prototype.render.call(this, $parent);
  if (this.installFocusContext) {
     this.$container.installFocusContextAsync('auto', this.session.uiSessionId);
  }
  this._attachCloseHandler();
  this.position();
};

scout.Popup.prototype.remove = function() {
  if (!this.rendered) {
    return;
  }
  if (this.installFocusContext) {
    this.$container.uninstallFocusContext(this.session.uiSessionId);
  }
  scout.Popup.parent.prototype.remove.call(this);
  // remove all clean-up handlers
  this._detachCloseHandler();
  this.rendered = false;
};

scout.Popup.prototype._render = function($parent) {
  if (!$parent) {
    $parent = this.session.$entryPoint;
  }
  this.$container = $.makeDiv('popup')
    .appendTo($parent);
};

scout.Popup.prototype.close = function(event) {
  this.remove();
};

/**
 * click outside container, scroll down closes popup
 */
scout.Popup.prototype._attachCloseHandler = function() {
  this._mouseDownHandler = this._onMouseDown.bind(this);
  $(document).on('mousedown', this._mouseDownHandler);

  if (this.$anchor) {
    this._scrollHandler = this._onAnchorScroll.bind(this);
    scout.scrollbars.onScroll(this.$anchor, this._scrollHandler);
  }
};

scout.Popup.prototype._detachCloseHandler = function() {
  if (this._scrollHandler) {
    scout.scrollbars.offScroll(this._scrollHandler);
    this._$scrollParents = null;
  }
  if (this._mouseDownHandler) {
    $(document).off('mousedown', this._mouseDownHandler);
    this._mouseDownHandler = null;
  }
};

scout.Popup.prototype._onMouseDown = function(event) {
  var $target = $(event.target);
  // close the popup only if the click happened outside of the popup
  if (this.$container && this.$container.has($target).length === 0) {
    this._onMouseDownOutside(event);
  }
};

scout.Popup.prototype._onMouseDownOutside = function(event) {
  this.close(event);
};

scout.Popup.prototype._onAnchorScroll = function(event) {
  this.remove();
};

scout.Popup.prototype.prefLocation = function($container, openingDirectionY) {
  var x, y, anchorBounds, height;
  if (!this.anchorBounds && !this.$anchor) {
    return;
  }
  openingDirectionY = openingDirectionY || 'down';
  $container.removeClass('up down');
  $container.addClass(openingDirectionY);
  height = $container.outerHeight(true),

  anchorBounds = this.anchorBounds;
  if (!anchorBounds) {
    anchorBounds = this.$anchor && scout.graphics.offsetBounds(this.$anchor);
  }

  x = anchorBounds.x;
  y = anchorBounds.y;
  if (openingDirectionY === 'up') {
    y -= height;
  } else if (openingDirectionY === 'down'){
    y += anchorBounds.height;
  }
  return {x: x, y: y};
};

scout.Popup.prototype.overlap = function($container, location, anchorBounds) {
  if (!$container || !location) {
    return;
  }
  var overlapX, overlapY,
    height = $container.outerHeight(),
    width = $container.outerWidth(),
    left = location.x,
    top = location.y;

  overlapX = left + width + this.windowPaddingX - $(window).width();
  overlapY = top + height + this.windowPaddingY  - $(window).height();
  return {x: overlapX, y: overlapY};
};

scout.Popup.prototype.adjustLocation = function($container, location, anchorBounds) {
  var openingDirection, left, top,
    overlap = this.overlap($container, location, anchorBounds);

  if (overlap.y > 0) {
    // switch opening direction
    openingDirection = 'up';
    location = this.prefLocation($container, openingDirection);
  }
  left = location.x,
  top = location.y;
  if (overlap.x > 0) {
    // Move popup to the left until it gets fully visible
    left -= overlap.x;
  }
  return {x: left, y: top};
};

scout.Popup.prototype.position = function() {
  this._position(this.$container);
};

scout.Popup.prototype._position = function($container) {
  var location = this.prefLocation($container);
  if (!location) {
    return;
  }
  location = this.adjustLocation($container, location);
  this.setLocation(location);
};

scout.Popup.prototype.setLocation = function(location) {
  this.$container
    .css('left', location.x)
    .css('top', location.y);
};

scout.Popup.prototype._createKeyStrokeAdapter = function() {
  return new scout.PopupKeyStrokeAdapter(this);
};
