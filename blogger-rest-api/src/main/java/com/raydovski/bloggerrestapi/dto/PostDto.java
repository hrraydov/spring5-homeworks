package com.raydovski.bloggerrestapi.dto;

import java.time.LocalDate;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

	private String id;

	private LocalDate publishedOn;

	private String title;

	private String author;

	private String text;

	private Collection<String> keywords;

	private String imageUrl;

	private boolean active;

}
