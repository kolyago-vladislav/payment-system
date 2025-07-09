package com.example.aspect;

import lombok.RequiredArgsConstructor;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

import com.example.metric.LoginCountTotalMetric;

@Aspect
@Component
@RequiredArgsConstructor
public class LoginMetricsAspect {

    private final LoginCountTotalMetric loginCountTotalMetric;

    @AfterReturning("execution(public * com.example.service.TokenService.login(..))")
    public void afterLogin() {
        loginCountTotalMetric.incrementCounter();
    }
}