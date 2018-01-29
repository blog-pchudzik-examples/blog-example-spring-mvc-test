package com.pchudzik.blog.example.mvctest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ArticleService {
	public List<ArticleDto> findAll() {
		return Collections.emptyList();
	}

	public UUID createArticle(ArticleContentDto newArticle) {
		return UUID.randomUUID();
	}

	public void deleteArticle(UUID id) {
	}
}
