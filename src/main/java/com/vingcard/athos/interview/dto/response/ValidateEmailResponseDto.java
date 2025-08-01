package com.vingcard.athos.interview.dto.response;

public record ValidateEmailResponseDto(boolean validated,
                                       String email,
                                       String message) {
}
