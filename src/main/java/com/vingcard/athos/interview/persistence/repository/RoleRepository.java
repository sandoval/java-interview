package com.vingcard.athos.interview.persistence.repository;

import com.vingcard.athos.interview.model.enums.RoleEnum;
import com.vingcard.athos.interview.persistence.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Role> {
	List<Role> findByRole(RoleEnum roleEnum);
}
