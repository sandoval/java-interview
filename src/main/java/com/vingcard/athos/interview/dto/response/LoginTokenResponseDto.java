package com.vingcard.athos.interview.dto.response;

public record LoginTokenResponseDto(String accessToken, String refreshToken, Integer expiresIn) {
}
