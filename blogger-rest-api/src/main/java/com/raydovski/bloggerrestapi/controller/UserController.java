package com.raydovski.bloggerrestapi.controller;

import java.net.URI;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raydovski.bloggerrestapi.dto.UserDto;
import com.raydovski.bloggerrestapi.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(path = "/api/users")
@PreAuthorize("hasAuthority('Administrator')")
@Api(tags = "users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping
	@ApiOperation(authorizations = @Authorization(value = "Bearer"), value = "Get all users")
	public ResponseEntity<Collection<UserDto>> getAll() {
		return ResponseEntity.ok(this.userService.getAll());
	}

	@PostMapping(path = "/")
	@ApiOperation(authorizations = @Authorization(value = "Bearer"), value = "Create new user")
	public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userData) {
		UserDto result = this.userService.create(userData);
		return ResponseEntity.created(URI.create("/users/" + result.getId())).body(result);
	}

	@GetMapping(path = "/{id}")
	@ApiOperation(authorizations = @Authorization(value = "Bearer"), value = "Get user by id")
	public ResponseEntity<UserDto> getById(@PathVariable String id) {
		return ResponseEntity.ok(this.userService.getById(id));
	}

	@PutMapping(path = "/{id}")
	@ApiOperation(authorizations = @Authorization(value = "Bearer"), value = "Edit user by id")
	public ResponseEntity<UserDto> edit(@PathVariable String id, @Valid @RequestBody UserDto userData) {
		return ResponseEntity.ok(this.userService.edit(id, userData));
	}

	@DeleteMapping(path = "/{id}")
	@ApiOperation(authorizations = @Authorization(value = "Bearer"), value = "Delete user by id")
	public ResponseEntity<UserDto> delete(@PathVariable String id) {
		return ResponseEntity.ok(this.userService.delete(id));
	}

}
