package com.revature.testing;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
	
	@InjectMocks
	ZuulAspects testZuulAspects;

}
