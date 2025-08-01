package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.exception.NotFoundExceptionResponse;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLinkId;
import com.vingcard.athos.interview.persistence.repository.LockGatewayLinkRepository;
import com.vingcard.athos.interview.service.LockGatewayLinkService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LockGatewayLinkServiceImpl implements LockGatewayLinkService {

	private final LockGatewayLinkRepository linkRepository;


	/**
	 * Return all existing Lock and Gateway links
	 *
	 * @return List of Lock and Gateway link
	 */
	@Override
	public List<LockGatewayLink> getAllLinks() {
		return linkRepository.findAll();
	}


	/**
	 * Return existing link by lock serial ID
	 *
	 * @param lockSerial Lock Serial ID
	 * @return List of link object
	 */
	@Override
	public List<LockGatewayLink> getLinksByLockSerial(String lockSerial) {
		List<LockGatewayLink> link = linkRepository.findByLockSerial(lockSerial);

		if (link.isEmpty()) {
			throw new NotFoundExceptionResponse("Lock not found with serial: " + lockSerial);
		}

		return link;
	}


	/**
	 * Return existing link by gateway serial ID
	 *
	 * @param gatewaySerial Gateway Serial ID
	 * @return List of link object
	 */
	@Override
	public List<LockGatewayLink> getLinksByGatewaySerial(String gatewaySerial) {
		List<LockGatewayLink> link = linkRepository.findByGatewaySerial(gatewaySerial);

		if (link.isEmpty()) {
			throw new NotFoundExceptionResponse("Gateway not found with serial: " + gatewaySerial);
		}

		return link;
	}


	/**
	 * Return lock and gateway link by Lock Serial ID and Gateway Serial ID
	 *
	 * @param lockSerial    Lock Serial ID
	 * @param gatewaySerial Gateway Serial ID
	 * @return Status code and Lock and gateway link filtered by Lock serial ID and Gateway serial ID
	 */
	@Override
	public ResponseEntity<LockGatewayLink> getLink(String lockSerial, String gatewaySerial) {
		LockGatewayLinkId id = new LockGatewayLinkId(lockSerial, gatewaySerial);
		Optional<LockGatewayLink> link = linkRepository.findById(id);
		return link.map(ResponseEntity::ok).orElseThrow(() -> new NotFoundExceptionResponse("Lock not found with serial: " + id));
	}


	/**
	 * Register new lock and gateway link
	 *
	 * @param link Lock gateway object
	 * @return Newly gateway link created
	 */
	@Override
	public LockGatewayLink createLink(LockGatewayLink link) {
		if (link.getLockSerial() == null || link.getLockSerial().trim().isEmpty()) {
			throw new IllegalArgumentException("Lock serial cannot be empty");
		}

		if (link.getGatewaySerial() == null || link.getGatewaySerial().trim().isEmpty()) {
			throw new IllegalArgumentException("Gateway serial cannot be empty");
		}

		return linkRepository.save(link);
	}


	/**
	 * Update existing gateway and lock link from database
	 *
	 * @param lockSerial    Lock Serial ID
	 * @param gatewaySerial Gateway Serial ID
	 * @param linkDetails   Lock gateway Object
	 * @return Status code and Lock gateway link object
	 */
	@Override
	public ResponseEntity<LockGatewayLink> updateLink(String lockSerial,
	                                                  String gatewaySerial,
	                                                  LockGatewayLink linkDetails) {
		LockGatewayLinkId id = new LockGatewayLinkId(lockSerial, gatewaySerial);
		Optional<LockGatewayLink> optionalLink = linkRepository.findById(id);

		if (optionalLink.isPresent()) {
			LockGatewayLink link = optionalLink.get();
			link.setRssi(linkDetails.getRssi());
			return ResponseEntity.ok(linkRepository.save(link));
		} else {
			throw new NotFoundExceptionResponse("Lock not found with serial: " + id);
		}
	}


	/**
	 * @param lockSerial    Lock Serial ID
	 * @param gatewaySerial Gateway Serial ID
	 * @return Status code from link deletion
	 */
	@Override
	public ResponseEntity<?> deleteLink(String lockSerial, String gatewaySerial) {
		LockGatewayLinkId id = new LockGatewayLinkId(lockSerial, gatewaySerial);
		if (linkRepository.existsById(id)) {
			linkRepository.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			throw new NotFoundExceptionResponse("Lock not found with serial: " + id);
		}
	}
}
