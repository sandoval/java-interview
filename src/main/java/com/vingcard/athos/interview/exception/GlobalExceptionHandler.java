package com.vingcard.athos.interview.exception;

import com.vingcard.athos.interview.model.dto.response.ExceptionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
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

	@ExceptionHandler(NotFoundExceptionResponse.class)
	public ResponseEntity<ExceptionResponseDto> handleNotFoundException(NotFoundExceptionResponse e,
	                                                                    HttpServletRequest request,
	                                                                    HttpServletResponse response) {
		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponseDto(
				request.getRequestURI(),
				"Resource Not Found",
				e.getMessage(),
				Instant.now().toString(),
				HttpStatus.NOT_FOUND.value()
		));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponseDto> handleGenericException(Exception e,
	                                                                   HttpServletRequest request,
	                                                                   HttpServletResponse response) {
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponseDto(
				request.getRequestURI(),
				"Internal Server Error",
				e.getMessage(),
				Instant.now().toString(),
				HttpStatus.INTERNAL_SERVER_ERROR.value()
		));
	}
} 