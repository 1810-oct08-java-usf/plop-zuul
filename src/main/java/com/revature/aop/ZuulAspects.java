package com.revature.aop;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;



@Aspect
@Configuration
public class ZuulAspects {
	
	//Logger logger =  LoggerFactory.getLogger(this.getClass());
	
//	@Before("execution(public * com.revature..*(..))")
//	public void beforeAnything(JoinPoint joinPoint) {
//		System.out.println("Before Anything" + joinPoint.getKind());
//		System.out.println(joinPoint.getStaticPart() + "\n");
//	}
	
	@AfterThrowing(pointcut = "execution(* com.revature..*(..))", throwing = "ex")
	public void errorOcurance(JoinPoint joinPoint, Exception ex){
		System.out.println("An Error Has Occured");
		//logger.error(ex.getMessage());
	}
	
	@After("execution(* com.revature.security.JwtTokenAuthenticationFilter.doFilterInternal(..))")
	public void afterDoFilterInternal(JoinPoint joinPoint){
		HttpServletRequest request =  (HttpServletRequest) joinPoint.getArgs()[0];
		HttpServletResponse response =  (HttpServletResponse) joinPoint.getArgs()[1];
		HttpServletRequest httpRequest = request;
		Enumeration<String> ems2 = httpRequest.getHeaderNames();
		while (ems2.hasMoreElements()) {
			String temp = ems2.nextElement();
			System.out.println(temp + "    " + httpRequest.getHeader(temp));
		}
		
//		Enumeration<String> ems2 = response.getHeaderNames();
//		while (ems2.hasMoreElements()) {
//			String temp = ems2.nextElement();
//			System.out.println(temp + "    " + httpRequest.getHeader(temp));
//		}
	}
}
