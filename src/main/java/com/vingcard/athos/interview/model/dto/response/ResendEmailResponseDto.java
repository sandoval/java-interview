package com.vingcard.athos.interview.model.dto.response;

public record ResendEmailResponseDto(int status,
                                     boolean success,
                                     String email,
                                     String message) {
}
