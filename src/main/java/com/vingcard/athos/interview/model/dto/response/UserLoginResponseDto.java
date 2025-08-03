package com.vingcard.athos.interview.model.dto.response;

public record UserLoginResponseDto(
		String tokenType,
		String accessToken,
		String refreshToken,
		Integer expiresIn
) {
}
