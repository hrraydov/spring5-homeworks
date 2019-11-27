package com.raydovski.bloggerrestapi.controller;

import java.net.URI;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raydovski.bloggerrestapi.dto.PostDto;
import com.raydovski.bloggerrestapi.service.PostService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "/api/posts")
@Api(tags = "posts")
public class PostController {

	@Autowired
	private PostService postService;

	@GetMapping
	@ApiOperation(value = "Get all posts")
	public ResponseEntity<Collection<PostDto>> getAll() {
		return ResponseEntity.ok(this.postService.getAll());
	}

	@PostMapping(path = "/")
	@ApiOperation(authorizations = @Authorization(value = "Bearer"), value = "Create new post")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<PostDto> create(@Valid @RequestBody PostDto postData) {
		return ResponseEntity.created(URI.create("/posts/" + postData.getId())).body(this.postService.create(postData));
	}

	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Get post by id")
	public ResponseEntity<PostDto> getById(@PathVariable String id) {
		return ResponseEntity.ok(this.postService.getById(id));
	}

	@PutMapping(path = "/{id}")
	@PreAuthorize("hasAuthority('Administrator') or @postService.isLoggedInUserPostAuthor(#id)")
	@ApiOperation(authorizations = @Authorization(value = "Bearer"), value = "Edit post by id")
	public ResponseEntity<PostDto> edit(@PathVariable String id, @Valid @RequestBody PostDto postData) {
		return ResponseEntity.ok(this.postService.edit(id, postData));
	}

	@DeleteMapping(path = "/{id}")
	@PreAuthorize("@postService.isLoggedInUserPostAuthor(#id)")
	@ApiOperation(authorizations = @Authorization(value = "Bearer"), value = "Delete post by id")
	public ResponseEntity<PostDto> delete(@PathVariable String id) {
		return ResponseEntity.ok(this.postService.delete(id));
	}
}
