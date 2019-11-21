package com.revature.aop;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class ZuulAspects {

  @AfterThrowing(pointcut = "execution(* com.revature..*(..))", throwing = "ex")
  public void errorOcurance(JoinPoint joinPoint, Exception ex) {
    System.out.println("An Error Has Occured");
  }
  /** @param joinPoint */
  @Before("execution(* com.revature.security.JwtTokenAuthenticationFilter.doFilterInternal(..))")
  public void beforeDoFilterInternal(JoinPoint joinPoint) {
    System.out.println("Inside of aop before");
    HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
    HttpServletRequest httpRequest = request;
    Enumeration<String> ems2 = httpRequest.getHeaderNames();
    while (ems2.hasMoreElements()) {
      String temp = ems2.nextElement();
      System.out.println(temp + "    " + httpRequest.getHeader(temp));
    }
  }

  /** @param joinPoint */
  @After("execution(* com.revature.security.JwtTokenAuthenticationFilter.doFilterInternal(..))")
  public void afterDoFilterInternal(JoinPoint joinPoint) {
    System.out.println("Inside of aop after");
    HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
    HttpServletRequest httpRequest = request;
    Enumeration<String> ems2 = httpRequest.getHeaderNames();
    while (ems2.hasMoreElements()) {
      String temp = ems2.nextElement();
      System.out.println(temp + "    " + httpRequest.getHeader(temp));
    }
  }
}
