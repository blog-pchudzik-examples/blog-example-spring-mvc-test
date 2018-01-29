package com.pchudzik.blog.example.mvctest

import com.pchudzik.springmock.infrastructure.annotation.AutowiredMock
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.util.IdGenerator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ExceptionThrowingController.class)
class MvcExceptionHandlerTest extends Specification {
	@AutowiredMock
	IdGenerator idGenerator

	@Autowired
	MockMvc mvc

	@Test
	def "should handle IllegalStateException"() {
		given:
		final message = "Message text"
		final uuid = UUID.randomUUID()

		and:
		idGenerator.generateId() >> uuid

		when:
		final request = mvc
				.perform(MockMvcRequestBuilders.post("/exceptions")
				.param("message", message)
				.param("exceptionClass", IllegalStateException.class.name))

		then:
		request
				.andExpect(status().isBadRequest())

				.andExpect(jsonPath('$').value(["message": message, "uuid": uuid.toString()]))

				.andExpect(jsonPath('$.message').value(message))
				.andExpect(jsonPath('$.uuid').value(uuid.toString()))
	}

	@RestController
	@RequestMapping("/exceptions")
	static class ExceptionThrowingController {
		@PostMapping
		def throwException(@RequestParam String message, @RequestParam Class<? extends Exception> exceptionClass) {
			throw exceptionClass.newInstance(message)
		}
	}
}
