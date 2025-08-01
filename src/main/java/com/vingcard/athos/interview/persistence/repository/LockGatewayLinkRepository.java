package com.vingcard.athos.interview.persistence.repository;

import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLinkId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LockGatewayLinkRepository extends JpaRepository<LockGatewayLink, LockGatewayLinkId> {

	List<LockGatewayLink> findByLockSerial(String lockSerial);

	List<LockGatewayLink> findByGatewaySerial(String gatewaySerial);

}
