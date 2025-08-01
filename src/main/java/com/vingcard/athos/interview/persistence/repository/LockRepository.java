package com.vingcard.athos.interview.persistence.repository;

import com.vingcard.athos.interview.persistence.entity.Lock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LockRepository extends JpaRepository<Lock, String> {
	Optional<Lock> findBySerial(String serial);
}
