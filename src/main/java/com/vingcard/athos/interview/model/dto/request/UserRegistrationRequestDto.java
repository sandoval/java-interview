package com.vingcard.athos.interview.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequestDto(
		@Email(message = "Invalid email format.") String email,
		@NotBlank(message = "Password is required.") String password,
		@NotBlank(message = "Phone number is required") String phoneNumber
) {
}
