package com.vingcard.athos.interview.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
		@NotBlank(message = "Email is required.") String email,
		@NotBlank(message = "Password is required.") String password
) {
}
