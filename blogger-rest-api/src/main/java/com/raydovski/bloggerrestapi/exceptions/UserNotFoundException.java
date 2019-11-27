package com.raydovski.bloggerrestapi.exceptions;

public class UserNotFoundException extends EntityNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1559508562929189753L;

	public UserNotFoundException() {
		super("User not found");
	}

}
