package com.vingcard.athos.interview.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vingcard.athos.interview.persistence.entity.Lock;

public interface LockRepository extends JpaRepository<Lock, String> {
}
