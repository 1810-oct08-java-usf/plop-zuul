package com.revature.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * A filter used to intercept all requests and validate the JWT, if present, in
 * the HTTP request header.
 */
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

	private final JwtConfig jwtConfig;

	/**
	 * Constructor for JwtTokenAuthenticationFilter that instantiates the JwtConfig
	 * field.
	 * 
	 * @param jwtConfig
	 *            Provides configuration for validating JWTs
	 */
	public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	/**
	 * Performs the JWT validation. If no Authorization header is present, the
	 * request is passed along to the next filter in the chain (in case of requests
	 * to unrestricted endpoints). The token is valid only if it has the proper
	 * prefix, a proper principal, and is unexpired.
	 * 
	 * @param req
	 *            Provides information regarding the HTTP request.
	 * 
	 * @param resp
	 *            Provides information regarding the HTTP response.
	 * 
	 * @param chain
	 *            Used to pass the HTTP request and response objects to the next
	 *            filter in the chain.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
			throws ServletException, IOException {

		/*
		 * 1. Get the authorization header. Tokens are supposed to be passed in the auth
		 * header
		 */
		String header = req.getHeader(jwtConfig.getHeader());

		/*
		 * 2. Validate the auth header and check the prefix (which we defined to be
		 * "Bearer ")
		 */
		if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
			chain.doFilter(req, resp);
			return;
		}

		/*
		 * If there is no token provided, this means that the user is not authenticated.
		 * It may seem wrong to send the request along the filter chain, but perhaps the
		 * user is accessing a public path or asking for a token.
		 * 
		 * All secured paths that needs a token are already defined and secured our
		 * SecurityTokenConfig class. If a user tried to access one of the secured paths
		 * without access token, then he won't be authenticated and an exception will be
		 * thrown.
		 */

		// 3. Get the token
		String token = header.replaceAll(jwtConfig.getPrefix(), "");

		// Exceptions might be thrown in creating the claims (i.e if the token expired)
		try {

			Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(token)
					.getBody();

			String username = claims.getSubject();

			/*
			 * 5. Create auth object
			 * 
			 * UsernamePasswordAuthenticationToken: A built-in object, used by Spring to
			 * represent the current authenticated / being authenticated user. It needs a
			 * list of authorities, which has type of GrantedAuthority interface, where
			 * SimpleGrantedAuthority is an implementation of that interface.
			 */
			if (username != null) {
				List<String> authorities = (List<String>) claims.get("authorities");

				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
						authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

				// 6. Authenticate the user
				SecurityContextHolder.getContext().setAuthentication(auth);
			}

		} catch (Exception e) {
			/*
			 * In case of failure, make sure it's clear; so we can guarantee that the user
			 * will not be authenticated
			 */
			SecurityContextHolder.clearContext();
		}

		// Go to the next filter in the filter chain
		chain.doFilter(req, resp);
	}

}