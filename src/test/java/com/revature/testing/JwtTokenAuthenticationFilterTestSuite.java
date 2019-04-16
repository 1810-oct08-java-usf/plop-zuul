package com.revature.testing;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.revature.aop.ZuulAspects;
import com.revature.security.JwtConfig;
import com.revature.security.JwtTokenAuthenticationFilter;
import com.revature.security.ZuulConfig;
import com.netflix.zuul.context.RequestContext;

/** 
 * Test Suite for the JwtTokenAuthenticationFilter class.  
 *  
 * @author Alonzo Muncy (190107 Java-Spark-USF)
 *  
 */

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenAuthenticationFilterTestSuite {
	@Mock
	HttpServletRequest mockHttpServletRequest;
	
	@Mock
	HttpServletResponse mockHttpServletResponse;
	
	@Mock
	RequestContext mockRequestContext;
	
	@Mock
	FilterChain mockFilterChain;
	
	@Mock 
	JwtConfig mockJwtConfig;

	@Mock
	ZuulConfig mockZuulConfig;
	
	
	@Spy
	@InjectMocks
	JwtTokenAuthenticationFilter classUnderTest;
	
	/**
	 * This tests doFilter in the adding Zuul header configuration.
	 */
	@Test
	public void testDoFilterInternalDoAddZullHeader () {
		String dummyString = "dummy";
		
		when(mockJwtConfig.getHeader()).thenReturn(dummyString);
		when(mockHttpServletRequest.getHeader(dummyString)).thenReturn(dummyString);
		when(mockJwtConfig.getPrefix()).thenReturn(dummyString);
		//when(classUnderTest.staticWrapperRequestContext()).thenReturn(mockRequestContext);
		//when(mockZuulConfig.getSalt()).thenReturn(dummyString);
		//when(mockZuulConfig.getSecret()).thenReturn(dummyString);
		//when(mockZuulConfig.getHeader()).thenReturn(dummyString);
		//when(classUnderTest.get_SHA_512_SecureHash(dummyString, dummyString)).thenReturn(dummyString);
		
		try {
			classUnderTest.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * This tests doFilter in the not adding Zuul header configuration.
	 */
	@Test
	public void testDoFilterInternalDontAddZullHeader () {
		String dummyString = "dummy";
		
		when(mockJwtConfig.getHeader()).thenReturn(null);
		when(mockHttpServletRequest.getHeader(null)).thenReturn(null);
		//when(mockJwtConfig.getPrefix()).thenReturn(dummyString);
		when(classUnderTest.staticWrapperRequestContext()).thenReturn(mockRequestContext);
		when(mockZuulConfig.getSalt()).thenReturn(dummyString);
		when(mockZuulConfig.getSecret()).thenReturn(dummyString);
		when(mockZuulConfig.getHeader()).thenReturn(null);
		when(classUnderTest.get_SHA_512_SecureHash(dummyString, dummyString)).thenReturn(dummyString);
		
		try {
			classUnderTest.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
