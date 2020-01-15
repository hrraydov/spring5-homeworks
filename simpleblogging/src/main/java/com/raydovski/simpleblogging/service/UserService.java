package com.raydovski.simpleblogging.service;

import java.util.List;
import java.util.Optional;

import com.raydovski.simpleblogging.entity.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    List<User> getAllUsers();

    Optional<User> getUserById(String id);

    User create(User user);

    User edit(User user);

    void delete(String id);
}