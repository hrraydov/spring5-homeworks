package com.raydovski.bloggerrestapi.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.authentication.AuthenticationProviderBeanDefinitionParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.raydovski.bloggerrestapi.dto.UserDto;
import com.raydovski.bloggerrestapi.service.AuthenticationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestAuthenticationProvider implements AuthenticationProvider {

	private AuthenticationService authService;
	
	public RestAuthenticationProvider(AuthenticationService authService) {
		this.authService = authService;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// get token from authentication
		String token = (String) authentication.getPrincipal();
		log.debug("Token: " + token);
		Authentication auth = new UserAuthentication(this.authService.getUserByToken(token), token);
		log.debug("Authorities: " + auth.getAuthorities());
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(TokenAuthentication.class);
	}

}
