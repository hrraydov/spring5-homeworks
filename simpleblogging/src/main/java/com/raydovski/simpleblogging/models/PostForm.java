package com.raydovski.simpleblogging.models;

import java.util.Arrays;

import com.raydovski.simpleblogging.entity.Post;
import com.raydovski.simpleblogging.entity.Role;

import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostForm {

    private String id;

    private String title;

    private String text;

    private String keywords;

    private boolean active;

    private String imageUrl;

    public static void copyTo(PostForm form, Post post) {
        BeanUtils.copyProperties(form, post, "keywords");
        post.setKeywords(Arrays.asList(form.getKeywords().split(",")));
    }

    public static PostForm from(Post post) {
        PostForm postForm = new PostForm();
        BeanUtils.copyProperties(post, postForm, "keywords");
        postForm.setKeywords(String.join(",", post.getKeywords()));
        return postForm;
    }

}