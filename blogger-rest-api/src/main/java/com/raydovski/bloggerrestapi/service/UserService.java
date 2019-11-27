package com.raydovski.bloggerrestapi.service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.raydovski.bloggerrestapi.dto.UserDto;
import com.raydovski.bloggerrestapi.entity.User;
import com.raydovski.bloggerrestapi.exceptions.UserNotFoundException;
import com.raydovski.bloggerrestapi.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	public Collection<UserDto> getAll() {
		return this.userRepository.findAll().stream().map(user -> this.userDtoFromUser(user))
				.collect(Collectors.toList());
	}

	public UserDto getById(String id) {
		User user = this.getEntityById(id);
		return this.userDtoFromUser(user);
	}

	public UserDto create(UserDto dto) {
		User user = this.userFromUserDto(dto);
		user.setId(UUID.randomUUID().toString());
		user.setPassword(this.passwordEncoder.encode(dto.getPassword()));
		user = this.userRepository.save(user);
		return this.userDtoFromUser(user);
	}

	public UserDto edit(String id, UserDto dto) {
		User user = this.getEntityById(id);
		user.setEmail(dto.getEmail());
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setRole(dto.getRole());
		user = this.userRepository.save(user);
		return this.userDtoFromUser(user);
	}

	public UserDto delete(String id) {
		User user = this.getEntityById(id);
		UserDto result = this.userDtoFromUser(user);
		this.userRepository.delete(user);
		return result;
	}

	public User getEntityById(String id) {
		return this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
	}

	public User getEntityByEmail(String email) {
		return this.userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException());
	}

	public UserDto userDtoFromUser(User user) {
		UserDto dto = new UserDto();
		BeanUtils.copyProperties(user, dto);
		return dto;
	}

	public User userFromUserDto(UserDto dto) {
		User user = new User();
		BeanUtils.copyProperties(dto, user, "id", "password");
		return user;
	}

}
