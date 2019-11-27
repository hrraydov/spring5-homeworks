package com.raydovski.bloggerrestapi.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TokenInvalidException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -228927198259522946L;

	public TokenInvalidException() {
		super("Token invalid");
	}

}
