package com.raydovski.bloggerrestapi.exceptions;

public class EntityNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1712811750500316613L;

	public EntityNotFoundException(String message) {
		super(message);
	}
}
