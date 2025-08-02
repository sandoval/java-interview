package com.vingcard.athos.interview.model.dto.response;

public record ValidateEmailResponseDto(boolean validated,
                                       String email,
                                       String message) {
}
