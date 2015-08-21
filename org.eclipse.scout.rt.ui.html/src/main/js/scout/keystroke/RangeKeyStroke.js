/**
 * KeyStroke which is bound to a range of keys, e.g. ctrl-1 ... ctrl-9.
 */
scout.RangeKeyStroke = function() {
  scout.RangeKeyStroke.parent.call(this);
  this.ranges = [];
};
scout.inherits(scout.RangeKeyStroke, scout.KeyStroke);

scout.RangeKeyStroke.prototype.registerRange = function(from, to) {
  this.ranges.push({
    from: from,
    to: to
  });
};

scout.RangeKeyStroke.prototype.accept = function(event) {
  return scout.RangeKeyStroke.parent.prototype.accept.call(this, event);
};

/**
 * @override KeyStroke.js
 */
scout.RangeKeyStroke.prototype._accept = function(event) {
  if (event.ctrlKey !== this.ctrl ||
    event.altKey !== this.alt ||
    event.shiftKey !== this.shift) {
    return false;
  }

  return this.ranges.some(function(range) {
    return event.which >= this._getRangeFrom(range) && event.which <= this._getRangeTo(range);
  }, this);
};

/**
 * @override KeyStroke.js
 */
scout.RangeKeyStroke.prototype.keys = function() {
  var keys = [];
  this.ranges.forEach(function(range) {
    var from = this._getRangeFrom(range);
    var to = this._getRangeTo(range);

    for (var which = from; which <= to; which++) {
      keys.push(new scout.Key(this, which));
    }
  }, this);

  return keys;
};

scout.RangeKeyStroke.prototype._getRangeFrom = function(range) {
  return (typeof range.from === 'function' ? range.from() : range.from);
};

scout.RangeKeyStroke.prototype._getRangeTo = function(range) {
  return (typeof range.to === 'function' ? range.to() : range.to);
};
