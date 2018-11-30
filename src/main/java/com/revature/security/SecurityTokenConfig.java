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
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // disables the protection against Cross-Site Request Forgery (CSRF), otherwise we cannot make external requests to the gateway service
            .csrf().disable()
            // make sure we use a stateless session; session will not be used to store user information/state
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                // handle any unauthorized attempts
                .exceptionHandling().authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
            .and()
                // add a filter to validate tokens with every request
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                // authorization request configuration
                .authorizeRequests()
                    // allow all requests attempting to access our auth-service using a POST request
                    .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                    // must be an admin if trying to access any admin endpoints (authentication is still required)
                    .antMatchers("/gallery/admin/**").hasRole("ADMIN")
                    // any other requests must be authenticated
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
