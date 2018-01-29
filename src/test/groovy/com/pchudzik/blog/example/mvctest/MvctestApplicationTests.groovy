package com.pchudzik.blog.example.mvctest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest
class MvctestApplicationTests extends Specification {

	@Autowired
	private ApplicationContext appCtx

	def "should load context"() {
		expect:
		appCtx != null
	}

}
