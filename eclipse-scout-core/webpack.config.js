/*
 * Copyright (c) 2010, 2023 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
module.exports = (env, args) => {
  args.resDirArray = ['res'];
  if (args.run === 'global') {
    return require('./webpack.config.global.js')(env, args);
  }
  if (args.run === 'testing') {
    return require('./webpack.config.testing.js')(env, args);
  }
  const baseConfig = require('@eclipse-scout/cli/scripts/webpack-defaults');
  const config = baseConfig(env, args);
  return {
    entry: {
      'eclipse-scout-core.esm': './src/index.ts'
    },
    // Clean is false because the last config will clean it
    ...baseConfig.libraryConfig(config)
  };
};
