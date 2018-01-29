package com.pchudzik.blog.example.mvctest

import com.pchudzik.springmock.infrastructure.annotation.AutowiredMock
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.util.IdGenerator
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ArticleController.class)
@AutowiredMock(doubleClass = IdGenerator.class)
class ArticleControllerTest extends Specification {
	@AutowiredMock
	ArticleService articleService

	@Autowired
	MockMvc mockMvc

	def "should return all articles"() {
		given:
		final firstArticle = new ArticleDto(UUID.randomUUID(), "article 1", "article 1 content")
		final secondArticle = new ArticleDto(UUID.randomUUID(), "article 2", "article 2 content")

		and:
		articleService.findAll() >> [firstArticle, secondArticle]

		when:
		final request = mockMvc.perform(get("/articles"))

		then:
		request.andExpect(jsonEqualTo("""[{
			uuid: "${firstArticle.uuid}",
			title: "${firstArticle.title}",
			intro: "${firstArticle.intro}"
		},{
			uuid: "${secondArticle.uuid}",
			title: "${secondArticle.title}",
			intro: "${secondArticle.intro}"
		}]"""))
	}

	def "should save new article"() {
		given:
		final articleUuid = UUID.randomUUID()
		final articleTitle = "title"
		final article = "article"

		when:
		final request = mockMvc.perform(post("/articles")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""{
					"title": "$articleTitle",
					"content": "$article"
				}"""))

		then:
		request
				.andExpect(status().isCreated())
				.andExpect(content().string("\"$articleUuid\""))

		and:
		1 * articleService.createArticle({ it.title == articleTitle && it.content == article }) >> articleUuid
	}

	def "should delete article"() {
		given:
		final id = UUID.randomUUID()

		when:
		final request = mockMvc.perform(MockMvcRequestBuilders.delete("/articles/$id"))

		then:
		request.andExpect(status().isNoContent())

		and:
		1 * articleService.deleteArticle(id)
	}

	private static ResultMatcher jsonEqualTo(String expected) {
		return new ResultMatcher() {
			@Override
			void match(MvcResult result) throws Exception {
				final actual = result.response.contentAsString
				JSONAssert.assertEquals(
						expected,
						actual,
						JSONCompareMode.STRICT)
			}
		}
	}
}
