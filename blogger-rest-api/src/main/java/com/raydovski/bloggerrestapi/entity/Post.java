package com.raydovski.bloggerrestapi.entity;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

	@Id
	private String id;

	private LocalDate publishedOn;
	
	private String createdBy;

	private String title;

	private String text;

	private Collection<String> keywords;

	private String imageUrl;

	private boolean active;
}
