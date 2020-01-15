package com.raydovski.simpleblogging.service;

import java.util.List;
import java.util.Optional;

import com.raydovski.simpleblogging.entity.Post;

public interface PostService {
    List<Post> getLast15Posts(boolean active);

    List<Post> getPosts();

    Optional<Post> getPostById(String id);

    Post create(Post post);

    Post edit(Post post);

    void delete(String id);
}