package com.raydovski.simpleblogging.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.raydovski.simpleblogging.entity.Post;
import com.raydovski.simpleblogging.entity.User;
import com.raydovski.simpleblogging.repository.PostRepository;
import com.raydovski.simpleblogging.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    public List<Post> getLast15Posts(boolean active) {
        List<Post> posts = this.postRepository.findAllByActive(active, PageRequest.of(0, 15, Sort.by(Direction.DESC, "publishedOn")))
                .getContent();
        posts.forEach(post -> {
            post.setCreatedBy(this.userService.getUserById(post.getCreatedBy()).get().getFirstName()
                    + this.userService.getUserById(post.getCreatedBy()).get().getLastName());
        });
        return posts;
    }

    @PostFilter(value = "(filterObject.createdBy == authentication.principal.id) or hasAuthority('Administrator')")
    public List<Post> getPosts() {
        List<Post> posts = this.postRepository.findAll();
        posts.forEach(post -> {
            post.setCreatedBy(this.userService.getUserById(post.getCreatedBy()).get().getFirstName()
                    + this.userService.getUserById(post.getCreatedBy()).get().getLastName());
        });
        return posts;
    }

    public Optional<Post> getPostById(String id) {
        return this.postRepository.findById(id);
    }

    public Post create(Post post) {
        post.setPublishedOn(LocalDate.now());
        post.setCreatedBy(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        return this.postRepository.insert(post);
    }

    public Post edit(Post post) {
        return this.postRepository.save(post);
    }

    public void delete(String id) {
        this.postRepository.deleteById(id);
    }

}