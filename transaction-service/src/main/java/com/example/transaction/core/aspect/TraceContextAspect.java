package com.example.transaction.core.aspect;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.core.util.JsonWrapper;
import com.example.transaction.model.entity.OutboxEvent;

import static com.example.transaction.core.util.TracingUtil.injectTraceIdInTracingContext;

@Aspect
@Component
@RequiredArgsConstructor
public class TraceContextAspect {

    private final JsonWrapper jsonWrapper;

    @Around("""
        execution(* com.example.transaction.entrypoint.listener.*.*(..))
        && !execution(* com.example.transaction.entrypoint.listener.OutboxEventConsumer.processMessage(..))
        && args(record)
    """)
    public void wrapKafkaListenerWithTraceId(ProceedingJoinPoint pjp, ConsumerRecord<String, byte[]> record) {
        var traceId = new String(record.headers().lastHeader("traceId").value());
        injectTraceIdInTracingContext(traceId, () -> proceed(pjp));
    }

    @Around("""
        execution(* com.example.transaction.entrypoint.listener.OutboxEventConsumer.processMessage(..))
        && args(event)
    """)
    public void wrapOutboxKafkaListenerWithTraceId(ProceedingJoinPoint pjp, byte[] event) {
        var outbox = jsonWrapper.read(event, OutboxEvent.class);
        injectTraceIdInTracingContext(outbox.getTraceId(), () -> proceed(pjp));
    }

    public void proceed(ProceedingJoinPoint pjp) {
        try {
            pjp.proceed();
        } catch (Throwable e) {
            throw new TransactionServiceException("Can not proceed aspect in class=%s with message=%s", getClass().getSimpleName(), e.getMessage());
        }
    }
}