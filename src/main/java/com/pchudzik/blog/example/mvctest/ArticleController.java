package com.pchudzik.blog.example.mvctest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
class ArticleController {
	private final ArticleService articleService;

	@GetMapping
	public List<ArticleDto> findAllArticles() {
		return articleService.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UUID createArticle(@RequestBody ArticleContentDto newArticle) {
		return articleService.createArticle(newArticle);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteArticle(@PathVariable UUID id) {
		articleService.deleteArticle(id);
	}
}
