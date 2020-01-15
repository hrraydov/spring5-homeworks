package com.raydovski.simpleblogging.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raydovski.simpleblogging.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);

}