package com.raydovski.bloggerrestapi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raydovski.bloggerrestapi.dto.Credentials;
import com.raydovski.bloggerrestapi.dto.UserDto;
import com.raydovski.bloggerrestapi.service.AuthenticationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class AuthController {

	@Autowired
	private AuthenticationService authenticationService;

	@GetMapping(path = "/me")
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(authorizations = @Authorization(value = "Bearer"), value = "Get information about the logged user")
	public ResponseEntity<UserDto> me() {
		Authentication authentication = this.authenticationService.getAuthentication();
		log.debug("Token: " + authentication.getCredentials());
		log.debug("Principal: " + authentication.getPrincipal());
		return ResponseEntity.ok(this.authenticationService.getUserByToken((String) authentication.getCredentials()));
	}

	@PostMapping(path = "/login")
	@ApiOperation(value = "Receive a token for a given credentials")
	public ResponseEntity<String> login(@Valid @RequestBody Credentials credentials) {
		return ResponseEntity.ok(this.authenticationService.login(credentials));
	}

	@PostMapping(path = "/logout")
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value = "Invalidates the token of the logged user", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<?> logout() {
		this.authenticationService.logout();
		return ResponseEntity.ok(null);
	}

}
