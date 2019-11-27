package com.raydovski.bloggerrestapi.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import com.raydovski.bloggerrestapi.dto.MessageDto;
import com.raydovski.bloggerrestapi.exceptions.BadCredentialsException;
import com.raydovski.bloggerrestapi.exceptions.EntityNotFoundException;
import com.raydovski.bloggerrestapi.exceptions.TokenInvalidException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = EntityNotFoundException.class)
	public ResponseEntity<MessageDto> entityNotFound(EntityNotFoundException e) {
		return ResponseEntity.status(404).body(new MessageDto(e.getMessage()));
	}

	@ExceptionHandler(value = TokenInvalidException.class)
	public ResponseEntity<MessageDto> tokenInvalid(TokenInvalidException e) {
		return ResponseEntity.status(401).body(new MessageDto(e.getMessage()));
	}
	
	@ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<MessageDto> badCredentials(BadCredentialsException e) {
		return ResponseEntity.status(401).body(new MessageDto(e.getMessage()));
	}

}
