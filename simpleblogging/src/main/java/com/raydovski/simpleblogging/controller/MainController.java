package com.raydovski.simpleblogging.controller;

import java.util.List;

import com.raydovski.simpleblogging.entity.Post;
import com.raydovski.simpleblogging.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private PostService postService;

    @GetMapping
    public String home(@RequestParam(required = false, defaultValue = "true") boolean active, ModelMap model) {
        List<Post> posts = this.postService.getLast15Posts(active);
        model.addAttribute("posts", posts);
        model.addAttribute("active", active);
        return "home";
    }

}