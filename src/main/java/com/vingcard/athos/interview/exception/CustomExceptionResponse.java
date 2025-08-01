package com.vingcard.athos.interview.exception;

import java.time.LocalDateTime;

public record CustomExceptionResponse(
		LocalDateTime timestamp,
		Boolean success,
		int code,
		String message
) {
}
