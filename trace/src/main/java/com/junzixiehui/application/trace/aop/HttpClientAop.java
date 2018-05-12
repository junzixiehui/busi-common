package com.junzixiehui.application.trace.aop;



import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;


@Slf4j
public class HttpClientAop {

  /**
   * 拦截HttpClient的Post与Get方法.
   * 
   */
  @Around("execution(* org.apache.http.client.HttpClient.execute(..))")
  public Object around(final ProceedingJoinPoint proceedingJoinPoint)
      throws Throwable {
    final long startTime = System.currentTimeMillis();
    final Object[] args = proceedingJoinPoint.getArgs();
    final Object result = proceedingJoinPoint.proceed(args);



    log.info("");



    return result;
  }
}
