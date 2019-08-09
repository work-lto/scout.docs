/*******************************************************************************
 * Copyright (c) 2014-2017 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
describe('App', function() {

  beforeEach(function() {
    setFixtures(sandbox().addClass('scout'));
  });

  describe('initDone', function() {
    it('is executed after desktop is rendered', function(done) {
      var app = new scout.App();
      app.init();

      var desktop;
      app.when('desktopReady').then(function(event) {
        desktop = event.desktop;
      });
      app.when('init')
        .then(function() {
          expect(desktop.rendered).toBe(true);
          expect(app.initialized).toBe(true);
        })
        .then(done)
        .catch(fail);
    });

    it('waits for load to complete', function(done) {
      var app = new scout.App();
      app.init();

      var loaded = false;
      app._load = function() {
        var def = $.Deferred();
        setTimeout(function() {
          loaded = true;
          def.resolve();
        });
        return def.promise();
      };
      app.when('init')
        .then(function() {
          expect(loaded).toBe(true);
          expect(app.initialized).toBe(true);
        })
        .then(done)
        .catch(fail);
    });

    it('is not executed when loading fails', function(done) {
      var app = new scout.App();
      app.init()
        .catch(function() {
          expect(app.initialized).toBe(false);
          done();
        });

      var loaded = false;
      app._load = function() {
        var def = $.Deferred();
        setTimeout(function() {
          loaded = true;
          def.reject();
        });
        return def.promise();
      };
    });
  });
});
