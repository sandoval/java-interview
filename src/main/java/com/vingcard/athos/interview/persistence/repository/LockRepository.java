package com.vingcard.athos.interview.persistence.repository;

import com.vingcard.athos.interview.persistence.entity.Lock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockRepository extends JpaRepository<Lock, String> {
}
