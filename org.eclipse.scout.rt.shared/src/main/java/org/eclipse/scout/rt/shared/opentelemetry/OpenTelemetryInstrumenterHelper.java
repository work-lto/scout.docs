/*
 * Copyright (c) 2010, 2024 BSI Business Systems Integration AG
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.scout.rt.shared.opentelemetry;

import java.util.List;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextKey;

@ApplicationScoped
public class OpenTelemetryInstrumenterHelper {
  public static final ContextKey<List<String>> NAME_CONTEXT_KEY = ContextKey.named("opentelemetry.name.key");

  public static void updateSpanName(String spanName) {
    List<String> o = Context.current().get(NAME_CONTEXT_KEY);
    if (CollectionUtility.hasElements(o)) {
      Span.current().updateName(spanName + " " + StringUtility.join(", ", o));
    }
  }

  public static void addNameToContext(String name) {
    List<String> o = Context.current().get(NAME_CONTEXT_KEY);
    if (o != null) {
      o.add(name);
    }
  }
}
