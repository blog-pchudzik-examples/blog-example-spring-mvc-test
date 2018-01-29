package com.pchudzik.blog.example.mvctest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.IdGenerator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
@RequiredArgsConstructor
public class MvcExceptionHandler {
	private final IdGenerator idGenerator;

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApplicationException handleIllegalStateException(IllegalStateException ex) {
		return new ApplicationException(
				ex.getMessage(),
				idGenerator.generateId());
	}

	@Getter
	@RequiredArgsConstructor
	public static class ApplicationException {
		private final String message;
		private final UUID uuid;
	}
}
