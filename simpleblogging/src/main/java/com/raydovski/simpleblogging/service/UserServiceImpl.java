package com.raydovski.simpleblogging.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.raydovski.simpleblogging.entity.User;
import com.raydovski.simpleblogging.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("UserService#loadUserByUsername called with username " + username);
        UserDetails userDetails = this.userRepository.findByEmail(username);
        log.debug("UserService#loadUserByUsername returns " + userDetails);
        return userDetails;
    }

    @PostFilter(value = "(filterObject.id == authentication.principal.id) or hasAuthority('Administrator')")
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return this.userRepository.findById(id);
    }

    public User create(User user) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return this.userRepository.insert(user);
    }

    public User edit(User user) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public void delete(String id) {
        this.userRepository.deleteById(id);
    }
}