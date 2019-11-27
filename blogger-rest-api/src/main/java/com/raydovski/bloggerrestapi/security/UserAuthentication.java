package com.raydovski.bloggerrestapi.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.raydovski.bloggerrestapi.dto.UserDto;

import lombok.ToString;

@ToString
public class UserAuthentication implements Authentication {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8461383457254308053L;
	private String token;
	private UserDto userDto;
	private UserDetails userDetails;

	public UserAuthentication(UserDto userDto, String token) {
		this.userDto = userDto;
		this.userDetails = new UserDtoUserDetails(userDto);
		this.token = token;
	}

	@Override
	public String getName() {
		return this.userDetails.getUsername();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.userDetails.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		return this.token;
	}

	@Override
	public Object getDetails() {
		return this.userDetails;
	}

	@Override
	public Object getPrincipal() {
		return this.userDto;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

}
