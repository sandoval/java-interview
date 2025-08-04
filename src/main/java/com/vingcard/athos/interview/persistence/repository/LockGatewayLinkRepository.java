package com.vingcard.athos.interview.persistence.repository;

import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.model.dto.LockGatewayLinkIdDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LockGatewayLinkRepository extends JpaRepository<LockGatewayLink, LockGatewayLinkIdDto> {

	List<LockGatewayLink> findByLockSerial(String lockSerial);

	List<LockGatewayLink> findByGatewaySerial(String gatewaySerial);

}
