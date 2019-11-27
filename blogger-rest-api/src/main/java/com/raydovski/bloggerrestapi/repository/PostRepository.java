package com.raydovski.bloggerrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raydovski.bloggerrestapi.entity.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

}
