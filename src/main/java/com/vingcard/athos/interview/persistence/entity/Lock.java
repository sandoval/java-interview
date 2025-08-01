package com.vingcard.athos.interview.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "lock")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Lock {

	@Id
	@Column(length = 16, nullable = false, unique = true)
	private String serial;

	@Column(length = 50, nullable = false)
	private String name;

	@Column(nullable = false)
	private String macAddress;

	@Column(nullable = false)
	private boolean online = false;

	@Column(length = 20, nullable = false)
	private String version = "";

}
