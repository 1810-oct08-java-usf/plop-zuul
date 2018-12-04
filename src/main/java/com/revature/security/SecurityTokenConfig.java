package com.revature.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.revature.security.JwtConfig;

@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

	private JwtConfig jwtConfig;
	
	/**
	 * Allows configuring web based security for specific http requests. By default it will be applied 
	 * to all requests, but can be restricted using requestMatcher(RequestMatcher) or other similar 
	 * methods.
	 * 
	 * @param http
	 * 		Used to configure Spring Security with regard to HTTP requests
	 */
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
	        /*
			 * Disables the protection against Cross-Site Request Forgery (CSRF), otherwise requests
			 * cannot be made to this request from the zuul-service.
			 */
            .csrf().disable()
            
            /* 
			 * Ensure that a stateless session is used; session will not be used to store user 
			 * information/state.
			 */
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            
            /*
			 * Handle any exceptions thrown during authentication by sending a response status
			 * of Authorized (401).
			 */
            .exceptionHandling()
            	.authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
            
        	/*
			 * Add a filter that will validate the token attached as an HTTP header
			 */
            .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
            
            /*
			 * Allows for the access to specific endpoints to be restricted and for others
			 * to be unrestricted
			 */
            .authorizeRequests()
            	
                // Allow all requests attempting to access our auth-service using a POST request
                .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                
                // Only admins can access any admin endpoints (authentication is still required)
                .antMatchers("/projects/admin/**").hasRole("ADMIN")
                
                /*
				 * TODO This needs to be refactored to restrict access to the actuator endpoints 
				 * to only admins
				 */
                // Only admins can access actuator endpoints (authentication is still required)
            	.antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
            	
            	/*
				 * TODO This needs to be refactored to restrict access to the auth-service 
				 * H2 console to only admins
				 */
				// Anyone can access the H2 console for the auth-service (for now)
            	.antMatchers(HttpMethod.GET, "/auth/h2-console/**").permitAll()
            	.antMatchers(HttpMethod.POST, "/auth/h2-console/**").permitAll()
                
            	// All other requests must be authenticated
                .anyRequest().authenticated();
    }
	
	@Autowired
	public void setJwtConfig(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}
	
	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}

}
