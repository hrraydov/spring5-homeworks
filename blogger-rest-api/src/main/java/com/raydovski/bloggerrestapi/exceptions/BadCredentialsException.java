package com.raydovski.bloggerrestapi.exceptions;

public class BadCredentialsException extends RuntimeException {
	
	public BadCredentialsException() {
		super("Bad Credentials");
	}
	
}
