package com.vingcard.athos.interview.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequestDto(
		@NotBlank(message = "Password is required.") String password,
		@Email(message = "Invalid email format.") String email
) {
}
