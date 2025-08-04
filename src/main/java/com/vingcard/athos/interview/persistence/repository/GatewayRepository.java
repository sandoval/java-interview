package com.vingcard.athos.interview.persistence.repository;

import com.vingcard.athos.interview.persistence.entity.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatewayRepository extends JpaRepository<Gateway, String> {
}
