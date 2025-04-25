package com.vingcard.athos.interview.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vingcard.athos.interview.persistence.entity.Gateway;

public interface GatewayRepository extends JpaRepository<Gateway, String> {
}
