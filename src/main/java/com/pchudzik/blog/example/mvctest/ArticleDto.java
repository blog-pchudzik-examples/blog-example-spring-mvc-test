package com.pchudzik.blog.example.mvctest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ArticleDto {
	private final UUID uuid;
	private final String title;
	private final String intro;
}
