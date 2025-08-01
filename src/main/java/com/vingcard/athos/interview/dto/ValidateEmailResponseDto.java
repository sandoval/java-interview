package com.vingcard.athos.interview.dto;

public record ValidateEmailResponseDto(boolean validated, String email, String message) {
}
