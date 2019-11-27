package com.raydovski.bloggerrestapi.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raydovski.bloggerrestapi.dto.MessageDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestAccessDenidedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		ObjectMapper mapper = new ObjectMapper();
		MessageDto dto = new MessageDto(accessDeniedException.getMessage());
		response.setContentType("application/json");
		response.setStatus(403);
		mapper.writeValue(response.getOutputStream(), dto);
		log.debug("DTO: " + dto);
		// response.sendError(403);
	}

}
