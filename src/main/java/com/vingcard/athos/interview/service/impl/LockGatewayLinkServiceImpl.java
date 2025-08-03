package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.exception.ResourceNotFoundException;
import com.vingcard.athos.interview.model.dto.LockGatewayLinkIdDto;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.persistence.repository.LockGatewayLinkRepository;
import com.vingcard.athos.interview.service.LockGatewayLinkService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
			throw new ResourceNotFoundException("Lock not found with serial: " + lockSerial);
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
			throw new ResourceNotFoundException("Gateway not found with serial: " + gatewaySerial);
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
		LockGatewayLinkIdDto id = new LockGatewayLinkIdDto(lockSerial, gatewaySerial);
		Optional<LockGatewayLink> link = linkRepository.findById(id);

		if (link.isEmpty()) {
			throw new ResourceNotFoundException("Gateway not found with serial: " + gatewaySerial);
		}

		return ResponseEntity.ok(link.get());
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
	 * @param id          Lock Gateway link ID with Lock Serial ID and Gateway Serial ID
	 * @param linkDetails Lock gateway Object
	 * @return Status code and Lock gateway link object
	 */
	@Override
	public LockGatewayLink updateLink(LockGatewayLinkIdDto id,
	                                  LockGatewayLink linkDetails) {
		Optional<LockGatewayLink> optionalLock = this.linkRepository.findById(id);

		if (optionalLock.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found");
		}

		optionalLock.get().setLockSerial(linkDetails.getLockSerial());
		optionalLock.get().setGatewaySerial(linkDetails.getGatewaySerial());
		optionalLock.get().setRssi(linkDetails.getRssi());

		return linkRepository.save(optionalLock.get());
	}


	/**
	 * Delete link from The Database
	 *
	 * @param id Lock Gateway link ID with Lock Serial ID and Gateway Serial ID
	 * @return Status code ok if success
	 */
	@Override
	public void deleteLink(LockGatewayLinkIdDto id) {

		LockGatewayLink lock = linkRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found: " + id));

		linkRepository.delete(lock);
	}


	/**
	 * Returns true if Link exists
	 *
	 * @param id Lock Gateway link ID with Lock Serial ID and Gateway Serial ID
	 * @return boolean
	 */
	@Override
	public boolean existsById(LockGatewayLinkIdDto id) {
		return linkRepository.existsById(id);
	}


	/**
	 * Find Link by Lock Serial ID and Gateway Serial ID
	 *
	 * @param id Lock Gateway link ID with Lock Serial ID and Gateway Serial ID
	 * @return Link Object
	 */
	@Override
	public Optional<LockGatewayLink> findById(LockGatewayLinkIdDto id) {
		if (linkRepository.existsById(id)) {
			return linkRepository.findById(id);
		} else {
			throw new ResourceNotFoundException("Link not found with serial: " + id);
		}
	}
}
