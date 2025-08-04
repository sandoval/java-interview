package com.vingcard.athos.interview.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "gateway")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Gateway {

	@Id
	@Column(length = 16, nullable = false, unique = true)
	@NotBlank(message = "Serial cannot be empty")
	private String serial;

	@Column(nullable = false)
	private String macAddress;

	@Column(nullable = false)
	private boolean online = false;

	@Column(length = 20, nullable = false)
	private String version = "";

}
