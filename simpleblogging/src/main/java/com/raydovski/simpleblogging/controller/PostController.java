package com.raydovski.simpleblogging.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.validation.Valid;

import com.raydovski.simpleblogging.entity.Post;
import com.raydovski.simpleblogging.models.PostForm;
import com.raydovski.simpleblogging.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Controller()
@Slf4j
@RequestMapping(path = "/posts")
public class PostController {

    private static final String UPLOADS_DIR = "tmp";

    @Autowired
    private PostService postService;

    @GetMapping
    public String getPosts(Model model) {
        model.addAttribute("path", "posts");
        model.addAttribute("posts", postService.getPosts());
        log.debug("GET Posts: " + postService.getPosts());
        return "posts";
    }

    @PostMapping(params = "edit")
    public String editPost(@RequestParam("edit") String editId, Model model, UriComponentsBuilder uriBuilder) {
        log.info("Editing post: " + editId);
        URI uri = uriBuilder.path("/posts/post-form").query("mode=edit&postId={id}").buildAndExpand(editId).toUri();
        return "redirect:" + uri.toString();
    }

    @PostMapping(params = "delete")
    public String deletePost(@RequestParam("delete") String deleteId, Model model) {
        log.info("Deleting post: " + deleteId);
        postService.delete(deleteId);
        return "redirect:/posts";
    }

    @GetMapping("/post-form")
    public String getPostForm(@ModelAttribute("post") PostForm post, ModelMap model,
            @RequestParam(value = "mode", required = false) String mode,
            @RequestParam(value = "postId", required = false) String postId) {
        String title = "Add New Post";
        if ("edit".equals(mode)) {
            Optional<Post> foundPost = postService.getPostById(postId);
            if (foundPost.isPresent()) {
                post = PostForm.from(foundPost.get());
                model.addAttribute("post", post);
                log.debug(model.toString());
                title = "Edit Post";
            }
        }
        return "post-form";
    }

    @PostMapping("/post-form")
    public String addPost(@Valid @ModelAttribute("post") PostForm post, BindingResult errors,
            @RequestParam("file") MultipartFile file, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("fileError", null);
            return "post-form";
        } else {
            log.info("POST Post: " + post);
            if (!file.isEmpty() && file.getOriginalFilename().length() > 0) {
                if (Pattern.matches("\\w+\\.(jpg|png)", file.getOriginalFilename())) {
                    handleMultipartFile(file);
                    post.setImageUrl(file.getOriginalFilename());
                } else {
                    model.addAttribute("fileError", "Submit picture [.jpg, .png]");
                    return "post-form";
                }
            }
            if (post.getId() == null) {
                Post dbPost = new Post();
                PostForm.copyTo(post, dbPost);
                log.info("ADD New Post: " + post);
                postService.create(dbPost);
            } else {
                Post dbPost = this.postService.getPostById(post.getId()).get();
                PostForm.copyTo(post, dbPost);
                log.info("UPDATE Post: " + post);
                postService.edit(dbPost);
            }
            return "redirect:/posts";
        }
    }

    private void handleMultipartFile(MultipartFile file) {
        String name = file.getOriginalFilename();
        long size = file.getSize();
        log.info("File: " + name + ", Size: " + size);
        try {
            File currentDir = new File(UPLOADS_DIR);
            if (!currentDir.exists()) {
                currentDir.mkdirs();
            }
            String path = currentDir.getAbsolutePath() + "/" + file.getOriginalFilename();
            path = new File(path).getAbsolutePath();
            log.info(path);
            File f = new File(path);
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(f));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}