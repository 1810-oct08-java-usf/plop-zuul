package com.revature.testing;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.revature.aop.ZuulAspects;

/** 
 * Test Suite for the Zuul Aspects class.  
 *  
 * @author Alonzo Muncy (190107 Java-Spark-USF)
 *  
 */

@RunWith(MockitoJUnitRunner.class)
public class ZuulAspectsTestSuite {
	
	@Mock
	JoinPoint mockJoinPoint;
	
	@Mock
	HttpServletRequest mockHttpServletRequest;
	
	@Mock
	HttpServletResponse mockHttpServletResponse;
	
	@InjectMocks
	ZuulAspects testZuulAspects;
	
	/**
	 * Testing the Before do filter function. TBH, I'm not sure why it exists. StringTokenizer is the only class I could find quickly that implements Enumeration. Enumeration should probably be Iterable, and some sane class that extends that instead.
	 */
	
	@Test
	public void testBeforeDoFilterInternal() {
		StringTokenizer st = new StringTokenizer("Silas Blackthorne");
		Enumeration enumString = st;
		
		Object arr[] = new Object[2];
		arr[0] = mockHttpServletRequest;
		
		when(mockJoinPoint.getArgs()).thenReturn(arr);
		when(mockHttpServletRequest.getHeaderNames()).thenReturn(enumString);
		when(mockHttpServletRequest.getHeader("Silas")).thenReturn("Silas");
		when(mockHttpServletRequest.getHeader("Blackthorne")).thenReturn("Blackthorne");
		
		testZuulAspects.beforeDoFilterInternal(mockJoinPoint);
		
		verify(mockHttpServletRequest, times(1)).getHeader("Silas");
		verify(mockHttpServletRequest, times(1)).getHeader("Blackthorne");
	}
	
	/**
	 * Exactly the same as before. Still not sure why this function exists.
	 * Testing the Before do filter function. TBH, I'm not sure why it exists. StringTokenizer is the only class I could find quickly that implements Enumeration. Enumeration should probably be Iterable, and some sane class that extends that instead.
	 */
	
	@Test
	public void testAfterDoFilterInternal() {
		StringTokenizer st = new StringTokenizer("Silas Blackthorne");
		Enumeration enumString = st;
		
		Object arr[] = new Object[2];
		arr[0] = mockHttpServletRequest;
		
		when(mockJoinPoint.getArgs()).thenReturn(arr);
		when(mockHttpServletRequest.getHeaderNames()).thenReturn(enumString);
		when(mockHttpServletRequest.getHeader("Silas")).thenReturn("Silas");
		when(mockHttpServletRequest.getHeader("Blackthorne")).thenReturn("Blackthorne");
		
		testZuulAspects.afterDoFilterInternal(mockJoinPoint);
		
		verify(mockHttpServletRequest, times(1)).getHeader("Silas");
		verify(mockHttpServletRequest, times(1)).getHeader("Blackthorne");
	}

}
