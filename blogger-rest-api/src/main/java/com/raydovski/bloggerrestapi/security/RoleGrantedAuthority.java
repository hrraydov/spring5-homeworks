package com.raydovski.bloggerrestapi.security;

import org.springframework.security.core.GrantedAuthority;

import com.raydovski.bloggerrestapi.entity.Role;

public class RoleGrantedAuthority implements GrantedAuthority {

	private Role role;

	public RoleGrantedAuthority(Role role) {
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return this.role.toString();
	}

	@Override
	public String toString() {
		return "RoleGrantedAuthority(" + role + ", " + this.getAuthority() + ")";
	}

}
