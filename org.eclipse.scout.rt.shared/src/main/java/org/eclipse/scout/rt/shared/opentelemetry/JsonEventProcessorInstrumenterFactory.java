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

import static io.opentelemetry.instrumentation.api.internal.AttributesExtractorUtil.internalSet;

import java.util.Objects;

import javax.annotation.Nullable;

import org.eclipse.scout.rt.platform.ApplicationScoped;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import io.opentelemetry.instrumentation.api.instrumenter.SpanKindExtractor;
import io.opentelemetry.instrumentation.api.instrumenter.SpanNameExtractor;

@ApplicationScoped
public class JsonEventProcessorInstrumenterFactory {
  public static final String INSTRUMENTATION_NAME = "scout.JsonEventProcessor";

  public static Instrumenter<OpenTelemetryJsonEventProcessorRequest, Void> createInstrumenter() {
    SpanNameExtractor<OpenTelemetryJsonEventProcessorRequest> spanNameExtractor = openTelemetryJsonEventRequest -> {
      String adapterModel = openTelemetryJsonEventRequest.getAdapterModelClass().getName().replaceAll(".*\\.", "");
      return adapterModel + "." + openTelemetryJsonEventRequest.getEventType();
    };
    AttributesExtractor<OpenTelemetryJsonEventProcessorRequest, Void> attributesExtractor = new AttributesExtractor<>() {
      private static final String SCOUT_PREFIX = "scout.client.json";

      @Override
      public void onStart(AttributesBuilder attributes, Context parentContext, OpenTelemetryJsonEventProcessorRequest openTelemetryJsonEventRequest) {
        internalSet(attributes, AttributeKey.stringKey(SCOUT_PREFIX + ".adapter"), openTelemetryJsonEventRequest.getAdapterModelClass().getName());
        internalSet(attributes, AttributeKey.stringKey(SCOUT_PREFIX + ".event"), openTelemetryJsonEventRequest.getEventType());
      }

      @Override
      public void onEnd(AttributesBuilder attributes, Context context, OpenTelemetryJsonEventProcessorRequest openTelemetryJsonEventRequest, @Nullable Void unused, @Nullable Throwable error) {
        // nop
      }
    };
    return Instrumenter.<OpenTelemetryJsonEventProcessorRequest, Void> builder(GlobalOpenTelemetry.get(),
        INSTRUMENTATION_NAME, spanNameExtractor)
        .addAttributesExtractor(attributesExtractor)
        // TODO[lto]: Enable/Disable
        .setEnabled(true)
        .buildInstrumenter(SpanKindExtractor.alwaysClient());
  }

  public static class OpenTelemetryJsonEventProcessorRequest<T> {
    private Class<T> m_adapterModelClass;
    private String m_eventType;

    public OpenTelemetryJsonEventProcessorRequest(Class<T> adapterModelClass, String eventType) {
      m_adapterModelClass = adapterModelClass;
      m_eventType = eventType;
    }

    public Class<T> getAdapterModelClass() {
      return m_adapterModelClass;
    }

    public void setAdapterModelClass(Class<T> adapterModelClass) {
      m_adapterModelClass = adapterModelClass;
    }

    public String getEventType() {
      return m_eventType;
    }

    public void setEventType(String eventType) {
      m_eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      OpenTelemetryJsonEventProcessorRequest that = (OpenTelemetryJsonEventProcessorRequest) o;
      return Objects.equals(m_adapterModelClass, that.m_adapterModelClass) && Objects.equals(m_eventType, that.m_eventType);
    }

    @Override
    public int hashCode() {
      return Objects.hash(m_adapterModelClass, m_eventType);
    }
  }
}
