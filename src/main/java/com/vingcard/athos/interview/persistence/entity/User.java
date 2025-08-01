package com.vingcard.athos.interview.persistence.entity;

import com.vingcard.athos.interview.enums.RoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
		name = "users",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "email"),
		})
public class User {
	@jakarta.persistence.Id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@NotEmpty(message = "Email is required")
	private String email;

	@Column
	@NotEmpty(message = "Password is required")
	private String password;

	@Column
	private Boolean enabled = true;

	@Column
	private Boolean verified = false;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createAt = LocalDateTime.now();

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime updateAt = LocalDateTime.now();

	@Column
	@Enumerated(EnumType.STRING)
	private RoleEnum role;

	@Transient
	private String accessToken;

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public LocalDateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
	}

	public LocalDateTime getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
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
		User user = (User) o;
		return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(enabled, user.enabled) && Objects.equals(verified, user.verified) && Objects.equals(createAt, user.createAt) && Objects.equals(updateAt, user.updateAt) && role == user.role && Objects.equals(accessToken, user.accessToken);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email, password, enabled, verified, createAt, updateAt, role, accessToken);
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", enabled=" + enabled +
				", verified=" + verified +
				", createAt=" + createAt +
				", updateAt=" + updateAt +
				", role=" + role +
				", accessToken='" + accessToken + '\'' +
				'}';
	}
}