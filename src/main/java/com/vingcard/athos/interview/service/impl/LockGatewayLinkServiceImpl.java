package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.exception.ResourceNotFoundException;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLinkId;
import com.vingcard.athos.interview.persistence.repository.LockGatewayLinkRepository;
import com.vingcard.athos.interview.service.LockGatewayLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LockGatewayLinkServiceImpl implements LockGatewayLinkService {

	private final LockGatewayLinkRepository linkRepository;

	@Autowired
	public LockGatewayLinkServiceImpl(LockGatewayLinkRepository linkRepository) {
		this.linkRepository = linkRepository;
	}

	@Override
	public List<LockGatewayLink> getAllLinks() {
		return linkRepository.findAll();
	}

	@Override
	public List<LockGatewayLink> getLinksByLockSerial(String lockSerial) {
		List<LockGatewayLink> link = linkRepository.findByLockSerial(lockSerial);

		if (link.isEmpty()) {
			throw new ResourceNotFoundException("Lock not found with serial: " + lockSerial);
		}

		return link;
	}

	@Override
	public List<LockGatewayLink> getLinksByGatewaySerial(String gatewaySerial) {
		List<LockGatewayLink> link = linkRepository.findByGatewaySerial(gatewaySerial);

		if (link.isEmpty()) {
			throw new ResourceNotFoundException("Gateway not found with serial: " + gatewaySerial);
		}

		return link;
	}

	@Override
	public ResponseEntity<LockGatewayLink> getLink(String lockSerial, String gatewaySerial) {
		LockGatewayLinkId id = new LockGatewayLinkId(lockSerial, gatewaySerial);
		Optional<LockGatewayLink> link = linkRepository.findById(id);
		return link.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Lock not found with serial: " + id));
	}

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
			throw new ResourceNotFoundException("Lock not found with serial: " + id);
		}
	}

	@Override
	public ResponseEntity<?> deleteLink(String lockSerial, String gatewaySerial) {
		LockGatewayLinkId id = new LockGatewayLinkId(lockSerial, gatewaySerial);
		if (linkRepository.existsById(id)) {
			linkRepository.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			throw new ResourceNotFoundException("Lock not found with serial: " + id);
		}
	}
}
