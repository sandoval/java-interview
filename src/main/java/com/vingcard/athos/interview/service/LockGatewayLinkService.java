package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.model.dto.LockGatewayLinkIdDto;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface LockGatewayLinkService {

	List<LockGatewayLink> getAllLinks();

	List<LockGatewayLink> getLinksByLockSerial(String lockSerial);

	List<LockGatewayLink> getLinksByGatewaySerial(String gatewaySerial);

	LockGatewayLink createLink(LockGatewayLink link);

	LockGatewayLink updateLink(LockGatewayLinkIdDto id, LockGatewayLink linkDetails);

	void deleteLink(LockGatewayLinkIdDto id);

	boolean existsById(LockGatewayLinkIdDto id);

	Optional<LockGatewayLink> findById(LockGatewayLinkIdDto id);

}
