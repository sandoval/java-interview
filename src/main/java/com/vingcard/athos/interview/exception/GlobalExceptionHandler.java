package com.vingcard.athos.interview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<CustomExceptionResponse> handleBadRequestExceptionException(IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(
				LocalDateTime.now(),
				false,
				HttpStatus.BAD_REQUEST.value(),
				e.getMessage()
		));
	}

	@ExceptionHandler(NotFoundExceptionResponse.class)
	public ResponseEntity<CustomExceptionResponse> handleNotFoundException(NotFoundExceptionResponse e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomExceptionResponse(
				LocalDateTime.now(),
				false,
				HttpStatus.NOT_FOUND.value(),
				e.getMessage()
		));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomExceptionResponse> handleGenericException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(
				LocalDateTime.now(),
				false,
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				e.getMessage()
		));
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<CustomExceptionResponse> handleResponseStatus(ResponseStatusException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomExceptionResponse(
				LocalDateTime.now(),
				false,
				HttpStatus.FORBIDDEN.value(),
				e.getMessage()
		));
	}
} 