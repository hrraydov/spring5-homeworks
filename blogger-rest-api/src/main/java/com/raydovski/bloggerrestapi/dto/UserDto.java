package com.raydovski.bloggerrestapi.dto;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.raydovski.bloggerrestapi.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

	private String id;

	private String firstName;

	private String lastName;

	private String email;

	@JsonIgnore
	private String password;

	private Role role;

	private String imageUrl;

}
