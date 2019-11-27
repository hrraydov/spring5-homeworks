package com.raydovski.bloggerrestapi.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raydovski.bloggerrestapi.dto.PostDto;
import com.raydovski.bloggerrestapi.dto.UserDto;
import com.raydovski.bloggerrestapi.entity.Post;
import com.raydovski.bloggerrestapi.entity.User;
import com.raydovski.bloggerrestapi.exceptions.PostNotFoundException;
import com.raydovski.bloggerrestapi.repository.PostRepository;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private UserService userService;

	public Collection<PostDto> getAll() {
		return this.postRepository.findAll().stream().map(post -> this.postDtoFromPost(post))
				.collect(Collectors.toList());
	}

	public PostDto getById(String id) {
		Post post = this.getEntityById(id);
		return this.postDtoFromPost(post);
	}

	public PostDto create(PostDto dto) {
		UserDto loggedUser = (UserDto) this.authenticationService.getAuthentication().getPrincipal();
		Post post = this.postFromPostDto(dto);
		post.setId(UUID.randomUUID().toString());
		post.setPublishedOn(LocalDate.now());
		post.setCreatedBy(loggedUser.getId());
		post.setActive(true);
		post = this.postRepository.save(post);
		return this.postDtoFromPost(post);
	}

	public PostDto edit(String id, PostDto dto) {
		Post post = this.getEntityById(id);
		post.setActive(dto.isActive());
		post.setImageUrl(dto.getImageUrl());
		post.setKeywords(dto.getKeywords());
		post.setText(dto.getText());
		post.setTitle(dto.getTitle());
		post = this.postRepository.save(post);
		return this.postDtoFromPost(post);
	}

	public PostDto delete(String id) {
		Post post = this.getEntityById(id);
		PostDto result = this.postDtoFromPost(post);
		this.postRepository.delete(post);
		return result;
	}

	public Post getEntityById(String id) {
		return this.postRepository.findById(id).orElseThrow(() -> new PostNotFoundException());
	}

	public boolean isLoggedInUserPostAuthor(String postId) {
		UserDto loggedUser = (UserDto) this.authenticationService.getAuthentication().getPrincipal();
		Post post = this.getEntityById(postId);
		return post.getCreatedBy().equals(loggedUser.getId());
	}

	public PostDto postDtoFromPost(Post post) {
		PostDto dto = new PostDto();
		BeanUtils.copyProperties(post, dto);
		UserDto author = this.userService.getById(post.getCreatedBy());
		dto.setAuthor(author.getFirstName() + author.getLastName());
		return dto;
	}

	public Post postFromPostDto(PostDto dto) {
		Post post = new Post();
		BeanUtils.copyProperties(dto, post, "id", "publishedOn", "createdBy");
		return post;
	}
}
