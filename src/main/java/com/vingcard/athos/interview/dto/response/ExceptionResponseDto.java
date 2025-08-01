package com.vingcard.athos.interview.dto.response;

public record ExceptionResponseDto(
		String path,
		String error,
		String message,
		String timestamp,
		int status
) {
}
