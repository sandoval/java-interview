package com.vingcard.athos.interview.model;

public record AuthTokens(
		String accessToken,
		String idToken,
		String refreshToken,
		int expiresIn,
		String tokenType
) {
}
