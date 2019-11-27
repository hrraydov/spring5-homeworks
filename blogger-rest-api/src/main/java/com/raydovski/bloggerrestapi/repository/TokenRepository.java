package com.raydovski.bloggerrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raydovski.bloggerrestapi.entity.Token;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

}
