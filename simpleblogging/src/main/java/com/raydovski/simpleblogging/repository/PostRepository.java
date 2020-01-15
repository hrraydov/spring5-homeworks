package com.raydovski.simpleblogging.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raydovski.simpleblogging.entity.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findAllByActive(boolean active, Pageable pageable);
}