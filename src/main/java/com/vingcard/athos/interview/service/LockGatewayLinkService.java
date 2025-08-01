package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LockGatewayLinkService {

	List<LockGatewayLink> getAllLinks();

	List<LockGatewayLink> getLinksByLockSerial(String lockSerial);

	List<LockGatewayLink> getLinksByGatewaySerial(String gatewaySerial);

	ResponseEntity<LockGatewayLink> getLink(String lockSerial, String gatewaySerial);

	LockGatewayLink createLink(LockGatewayLink link);

	ResponseEntity<LockGatewayLink> updateLink(String lockSerial,
	                                           String gatewaySerial,
	                                           LockGatewayLink linkDetails);

	ResponseEntity<?> deleteLink(String lockSerial, String gatewaySerial);
}
