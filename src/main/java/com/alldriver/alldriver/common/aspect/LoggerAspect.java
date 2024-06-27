package com.alldriver.alldriver.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LoggerAspect {

    @Before("execution(* com.alldriver.alldriver..*(..))")
    public void before(JoinPoint joinPoint) {
        log.info("[START] | where: " + joinPoint.toString());
    }

    @AfterReturning("execution(* com.alldriver.alldriver..*(..))")
    public void afterReturning(JoinPoint joinPoint) {
        log.info("[END] | where: " + joinPoint.toString());
    }

    @AfterThrowing(value = "execution(* com.alldriver.alldriver..*(..))", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        log.error("[END_WITH_FAIL] | where: " + joinPoint.toString());
        log.error("[END_WITH_FAIL] | exception: [" + exception + "]", exception);
    }
}