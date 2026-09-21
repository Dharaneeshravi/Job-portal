package com.dharaneesh.job_portal_backend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAndPerformanceAspect {

    @Around("execution(* com.dharaneesh.job_portal_backend..*.*(..))")
    public Object logAndMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime=System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("Entering method {}",methodName);
        log.info("Arguments {}",Arrays.toString(methodArgs));
        Object result = joinPoint.proceed();
        long executionTime=System.currentTimeMillis()-startTime;
        log.info("Method execution successfully:{}",methodName);
        log.info("Execution time {}",executionTime);
        return result;
    }
}
