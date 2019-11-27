package com.raydovski.bloggerrestapi.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.raydovski.bloggerrestapi.dto.Credentials;
import com.raydovski.bloggerrestapi.dto.UserDto;
import com.raydovski.bloggerrestapi.entity.Token;
import com.raydovski.bloggerrestapi.entity.User;
import com.raydovski.bloggerrestapi.exceptions.BadCredentialsException;
import com.raydovski.bloggerrestapi.exceptions.TokenInvalidException;
import com.raydovski.bloggerrestapi.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public String login(Credentials credentials) {
		try {
			User user = this.userService.getEntityByEmail(credentials.getEmail());
			if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
				throw new BadCredentialsException();
			}
			Token token = Token.builder().id(UUID.randomUUID().toString()).userId(user.getId()).build();
			token = this.tokenRepository.save(token);
			return token.getId();
		} catch (RuntimeException e) {
			throw new BadCredentialsException();
		}

	}

	public void logout() {
		Authentication auth = this.getAuthentication();
		String tokenId = (String) auth.getCredentials();
		this.tokenRepository
				.delete(this.tokenRepository.findById(tokenId).orElseThrow(() -> new TokenInvalidException()));
	}

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public UserDto getUserByToken(String token) {
		Token tokenEntity = this.tokenRepository.findById(token).orElseThrow(() -> new TokenInvalidException());
		User user = this.userService.getEntityById(tokenEntity.getUserId());
		return this.userService.userDtoFromUser(user);
	}

}
