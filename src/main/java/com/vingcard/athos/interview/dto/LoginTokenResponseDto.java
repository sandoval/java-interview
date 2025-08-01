package com.vingcard.athos.interview.dto;

public record LoginTokenResponseDto(String accessToken, String refreshToken, Integer expiresIn) {
}
