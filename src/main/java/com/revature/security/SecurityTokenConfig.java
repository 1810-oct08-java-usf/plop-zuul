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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

	private JwtConfig jwtConfig;
	private ZuulConfig zuulConfig;

	/**
	 * Allows configuring web based security for specific http requests. By default
	 * it will be applied to all requests, but can be restricted using
	 * requestMatcher(RequestMatcher) or other similar methods.
	 * 
	 * @param http Used to configure Spring Security with regard to HTTP requests
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println(zuulConfig.getHeader());
		System.out.println(zuulConfig.getSalt());
		System.out.println(zuulConfig.getSecret());
		http
				/*
				 * Disables the protection against Cross-Site Request Forgery (CSRF), otherwise
				 * requests cannot be made to this request from the zuul-service.
				 */
				.csrf().disable()

				/*
				 * Ensure that a stateless session is used; session will not be used to store
				 * user information/state.
				 */
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

				/*
				 * Handle any exceptions thrown during authentication by sending a response
				 * status of Authorized (401).
				 */
				.exceptionHandling()
				.authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				
				/*
				 * Add a filter that will validate the token attached as an HTTP header
				 */
				.addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig, zuulConfig), UsernamePasswordAuthenticationFilter.class)

				/*
				 * Allows for the access to specific endpoints to be restricted and for others
				 * to be unrestricted
				 */
				.authorizeRequests()

				.mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.mvcMatchers("/auth/**").permitAll()
				.mvcMatchers("/project/**").permitAll()
				//.mvcMatchers("/user/**").permitAll() //For the user service access
				.mvcMatchers(HttpMethod.GET, "/actuator/info").permitAll()

				// All other requests must be authenticated
				.anyRequest().authenticated();
	}

	@Autowired
	public void setJwtConfig(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@Bean
	public ZuulConfig zuulConfig() {
		return new ZuulConfig();
	}
	@Autowired
	public void setZuulConfig(ZuulConfig zuulConfig) {
		this.zuulConfig = zuulConfig;
	}

	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}
	
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addExposedHeader("authorization");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
