package com.vingcard.athos.interview.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLinkId;

public interface LockGatewayLinkRepository extends JpaRepository<LockGatewayLink, LockGatewayLinkId> {
}
