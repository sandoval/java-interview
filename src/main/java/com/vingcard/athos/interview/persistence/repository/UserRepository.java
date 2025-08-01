package com.vingcard.athos.interview.persistence.repository;

import com.vingcard.athos.interview.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	User findByEmailAndVerified(String email, boolean b);

}
