package com.vingcard.athos.interview.persistence.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vingcard.athos.interview.model.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private long id;

	@Column(nullable = false, unique = true)
	@Enumerated(EnumType.STRING)
	private RoleEnum role;

}
