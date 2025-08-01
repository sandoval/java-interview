package com.vingcard.athos.interview.requests;

import jakarta.validation.constraints.NotBlank;

public class UserLoginRequest {

	@NotBlank(message = "error.email_required")
	private String email;

	@NotBlank(message = "error.password_required")
	private String password;

	public UserLoginRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
