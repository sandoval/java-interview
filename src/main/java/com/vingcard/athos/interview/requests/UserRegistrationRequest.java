package com.vingcard.athos.interview.requests;

import com.vingcard.athos.interview.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class UserRegistrationRequest {

	@NotBlank(message = "Password is required")
	private String password;

	@Email(message = "Invalid email address")
	private String email;

	@NotNull(message = "Role is required")
	private RoleEnum role;

	public UserRegistrationRequest(String password, String email, RoleEnum role) {
		this.password = password;
		this.email = email;
		this.role = role;
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

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		UserRegistrationRequest that = (UserRegistrationRequest) o;
		return Objects.equals(password, that.password) && Objects.equals(email, that.email) && role == that.role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(password, email, role);
	}

	@Override
	public String toString() {
		return "UserRegistrationRequest{" +
				"password='" + password + '\'' +
				", email='" + email + '\'' +
				", role=" + role +
				'}';
	}
}
