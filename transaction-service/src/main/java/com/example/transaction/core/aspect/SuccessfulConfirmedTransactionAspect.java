package com.example.transaction.core.aspect;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

import com.example.transaction.dto.TransactionConfirmResponse;

@Slf4j
@Aspect
@Component
public class SuccessfulConfirmedTransactionAspect {

    @Around("execution(* com.example.transaction.business.service.transaction.confirm.base.ConfirmRequestHandler.handle(..))")
    public Object logAfterSuccessfulHandle(ProceedingJoinPoint pjp) throws Throwable {
        var result = (TransactionConfirmResponse) pjp.proceed();
        log.info("Transaction was confirmed successfully. TransactionId: {}", result.getTransactionId());
        return result;
    }
}