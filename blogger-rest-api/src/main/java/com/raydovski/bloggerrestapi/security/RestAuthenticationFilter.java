package com.raydovski.bloggerrestapi.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class RestAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String authorizationHeader = ((HttpServletRequest) request).getHeader("Authorization");
		logger.debug("Authorization header: " + authorizationHeader);
		if (authorizationHeader != null) {
			String token = authorizationHeader.substring(6).trim();
			logger.debug("Token: " + token);
			SecurityContextHolder.getContext().setAuthentication(new TokenAuthentication(token));
		}
		chain.doFilter(request, response);
	}

}
