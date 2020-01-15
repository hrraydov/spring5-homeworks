package com.raydovski.simpleblogging.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.validation.Valid;

import com.raydovski.simpleblogging.entity.User;
import com.raydovski.simpleblogging.service.UserService;
import com.raydovski.simpleblogging.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Controller()
@Slf4j
@RequestMapping(path = "/users")
public class UserController {

    private static final String UPLOADS_DIR = "tmp";

    @Autowired
    private UserService userService;

    @GetMapping
    public String getUsers(ModelMap model) {
        model.addAttribute("path", "users");
        model.addAttribute("users", userService.getAllUsers());
        log.debug("GET Users: " + userService.getAllUsers());
        return "users";
    }

    @PostMapping(params = "edit")
    public String editUser(@RequestParam("edit") String editId, Model model, UriComponentsBuilder uriBuilder) {
        log.info("Editing user: " + editId);
        URI uri = uriBuilder.path("/users/user-form").query("mode=edit&userId={id}").buildAndExpand(editId).toUri();
        return "redirect:" + uri.toString();
    }

    @PostMapping(params = "toggleActive")
    public String toggleActive(@RequestParam("toggleActive") String toggleId, Model model) {
        log.info("Toggle user: " + toggleId);
        User user = this.userService.getUserById(toggleId).get();
        user.setActive(!user.isActive());
        this.userService.edit(user);
        return "redirect:/users";
    }

    @GetMapping("/user-form")
    public String getUserForm(@ModelAttribute("user") User user, ModelMap model,
            @RequestParam(value = "mode", required = false) String mode,
            @RequestParam(value = "userId", required = false) String userId) {
        String title = "Add New User";
        if ("edit".equals(mode)) {
            Optional<User> foundUser = userService.getUserById(userId);
            if (foundUser.isPresent()) {
                user = foundUser.get();
                model.addAttribute("user", user);
                title = "Edit User";
            }
        }

        model.addAttribute("path", "user-form");
        model.addAttribute("title", title);
        return "user-form";
    }

    @PostMapping("/user-form")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult errors,
            @RequestParam("file") MultipartFile file, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("fileError", null);
            return "user-form";
        } else {
            log.info("POST User: " + user);
            if (!file.isEmpty() && file.getOriginalFilename().length() > 0) {
                if (Pattern.matches("\\w+\\.(jpg|png)", file.getOriginalFilename())) {
                    handleMultipartFile(file);
                    user.setImageUrl(file.getOriginalFilename());
                } else {
                    model.addAttribute("fileError", "Submit picture [.jpg, .png]");
                    return "user-form";
                }
            }
            if (user.getId() == null) {
                log.info("ADD New User: " + user);
                userService.create(user);
            } else {
                log.info("UPDATE User: " + user);
                userService.edit(user);
            }
            return "redirect:/users";
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