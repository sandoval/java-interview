package com.vingcard.athos.interview.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class UserRegistrationRequest {

	@NotBlank(message = "error.password_required")
	private String password;

	@Email(message = "error.invalid_email_format")
	private String email;

	public UserRegistrationRequest(String password, String email) {
		this.password = password;
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		UserRegistrationRequest that = (UserRegistrationRequest) o;
		return Objects.equals(password, that.password) && Objects.equals(email, that.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(password, email);
	}

	@Override
	public String toString() {
		return "UserRegistrationRequest{" +
				"password='" + password + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
