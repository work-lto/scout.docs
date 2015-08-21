scout.AbstractTreeNavigationKeyStroke = function(tree, modifierBitMask) {
  scout.AbstractTreeNavigationKeyStroke.parent.call(this);
  this.field = tree;
  this.stopPropagation = true;
  this.renderingHints.hAlign = scout.hAlign.RIGHT;

  this.ctrl = scout.keyStrokeModifier.isCtrl(modifierBitMask);
  this.shift = scout.keyStrokeModifier.isShift(modifierBitMask);
  this.alt = scout.keyStrokeModifier.isAlt(modifierBitMask);
};
scout.inherits(scout.AbstractTreeNavigationKeyStroke, scout.KeyStroke);

scout.AbstractTreeNavigationKeyStroke.prototype._accept = function(event) {
  var accepted = scout.AbstractTreeNavigationKeyStroke.parent.prototype._accept.call(this, event);
  if (!accepted) {
    return false;
  }

  var $currentNode = this.field.$selectedNodes().eq(0);
  event._$treeCurrentNode = $currentNode;
  event._treeCurrentNode = $currentNode.data('node');
  return true;
};

scout.AbstractTreeNavigationKeyStroke.prototype.handle = function(event) {
  var newNodeSelection = this._handleInternal(event._$treeCurrentNode, event._treeCurrentNode);
  if (newNodeSelection) {
    this.field.setNodesSelected(newNodeSelection);
    this.field.scrollTo(newNodeSelection);
  }
};

scout.AbstractTreeNavigationKeyStroke.prototype._handleInternal = function($currentNode, currentNode) {
  throw new Error('method must be overwritten by subclass');
};
