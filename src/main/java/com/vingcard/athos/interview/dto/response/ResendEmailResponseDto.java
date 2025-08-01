package com.vingcard.athos.interview.dto.response;

public record ResendEmailResponseDto(int status,
                                     boolean success,
                                     String email,
                                     String message) {
}
