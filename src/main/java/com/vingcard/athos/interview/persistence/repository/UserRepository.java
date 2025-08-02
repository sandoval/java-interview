package com.vingcard.athos.interview.persistence.repository;

import com.vingcard.athos.interview.persistence.entity.auth.Role;
import com.vingcard.athos.interview.persistence.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByEmailAndVerified(String email, boolean b);

	List<User> findAllByRoles(List<Role> roles);
}
