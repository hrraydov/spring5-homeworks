package com.raydovski.bloggerrestapi.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raydovski.bloggerrestapi.dto.MessageDto;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		MessageDto dto = new MessageDto(authException.getMessage());

		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), dto);
		response.sendError(401);
	}
}