package com.vingcard.athos.interview.persistence.entity;

import com.vingcard.athos.interview.enums.RoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

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

}