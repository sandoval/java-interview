package com.vingcard.athos.interview.persistence.entity.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
		name = "users",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "email")
		})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class User {

	@jakarta.persistence.Id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@NotEmpty(message = "Email is required")
	private String email;

	@Column
	@NotEmpty(message = "Phone Number is Required")
	private String phoneNumber;

	@Column
	@NotEmpty(message = "Password is required")
	private String password;

	@Column
	private Boolean enabled = false;

	@Column
	private Boolean verified = false;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createAt = LocalDateTime.now();

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime updateAt = LocalDateTime.now();

	@Column
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private List<Role> roles;

	@Transient
	private String accessToken;

}