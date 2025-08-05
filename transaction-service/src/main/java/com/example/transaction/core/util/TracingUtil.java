package com.example.transaction.core.util;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceState;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.IdGenerator;

public class TracingUtil {

    public static void injectTraceIdInTracingContext(String traceId, Runnable runnable) {
        var traceContext = SpanContext.createFromRemoteParent(
            traceId,
            IdGenerator.random().generateSpanId(),
            TraceFlags.getSampled(),
            TraceState.getDefault()
        );

        var parentContext = Context.root().with(Span.wrap(traceContext));

        try (var scope = parentContext.makeCurrent()) {
            runnable.run();
        }
    }
}