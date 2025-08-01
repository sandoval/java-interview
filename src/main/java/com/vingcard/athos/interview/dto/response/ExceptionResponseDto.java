package com.vingcard.athos.interview.dto.response;

import java.time.LocalDateTime;

public record ExceptionResponseDto(
		String path,
		String error,
		String message,
		String timestamp,
		int status
) {
}
