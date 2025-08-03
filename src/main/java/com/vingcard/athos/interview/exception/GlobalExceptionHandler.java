package com.vingcard.athos.interview.exception;

import com.vingcard.athos.interview.model.dto.response.ExceptionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionResponseDto> handleBadRequestExceptionException(IllegalArgumentException e,
	                                                                               HttpServletRequest request,
	                                                                               HttpServletResponse response) {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponseDto(
				request.getRequestURI(),
				"Bad Request",
				e.getMessage(),
				Instant.now().toString(),
				HttpStatus.BAD_REQUEST.value()
		));
	}
} 