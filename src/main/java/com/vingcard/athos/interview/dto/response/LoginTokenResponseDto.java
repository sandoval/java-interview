package com.vingcard.athos.interview.dto.response;

public record LoginTokenResponseDto(
		String tokenType,
		String accessToken,
		String refreshToken,
		Integer expiresIn
) {
}
