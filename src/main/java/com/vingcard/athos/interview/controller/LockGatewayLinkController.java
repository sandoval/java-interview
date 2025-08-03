package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.exception.ResourceNotFoundException;
import com.vingcard.athos.interview.model.dto.LockGatewayLinkIdDto;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.persistence.repository.LockGatewayLinkRepository;
import com.vingcard.athos.interview.service.LockGatewayLinkService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.query.NativeQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lock-gateway-links")
@AllArgsConstructor
public class LockGatewayLinkController {

	private final LockGatewayLinkService lockGatewayLinkService;
	private final LockGatewayLinkRepository lockGatewayLinkRepository;


	/**
	 * Get all links from database
	 * Path: /api/lock-gateway-links
	 * Method: Get
	 *
	 * @return Complete link list from Database
	 */
	@GetMapping
	public List<LockGatewayLink> getAllLinks() {
		return this.lockGatewayLinkService.getAllLinks();
	}


	/**
	 * Filter links by Lock Serial ID
	 * Path: /api/lock-gateway-links/{lockSerial}
	 * Method: GET
	 *
	 * @param lockSerial Lock Serial ID
	 * @return List of links by lock Serial ID Object
	 */
	@GetMapping("/lock/{lockSerial}")
	public List<LockGatewayLink> getLinksByLockSerial(@PathVariable String lockSerial) {
		return this.lockGatewayLinkService.getLinksByLockSerial(lockSerial);
	}


	/**
	 * Filter links by Serial ID
	 * Path: /api/lock-gateway-links/{gatewaySerial}
	 * Method: GET
	 *
	 * @param gatewaySerial Gateway Serial ID
	 * @return List of links by gateway Serial ID Object
	 */
	@GetMapping("/gateway/{gatewaySerial}")
	public List<LockGatewayLink> getLinksByGatewaySerial(@PathVariable String gatewaySerial) {
		return this.lockGatewayLinkService.getLinksByGatewaySerial(gatewaySerial);
	}


	/**
	 * Filter Lock and Gateway Link by Lock Serial ID and Gateway Serial ID
	 * Path: /api/lock-gateway-links/{lockSerial}/{gatewaySerial}
	 * Method: GET
	 *
	 * @param lockSerial    Lock Serial ID
	 * @param gatewaySerial Gateway Serial ID
	 * @return Lock Gateway Link Object by Lock Serial ID and Gateway Serial ID
	 */
	@GetMapping("/{lockSerial}/{gatewaySerial}")
	public ResponseEntity<LockGatewayLink> getLink(@PathVariable String lockSerial,
	                                               @PathVariable String gatewaySerial) {
		LockGatewayLinkIdDto id = new LockGatewayLinkIdDto(lockSerial, gatewaySerial);
		Optional<LockGatewayLink> link = lockGatewayLinkService.findById(id);

		if (link.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return link.map(ResponseEntity::ok)
				.orElseThrow(() -> new ResourceNotFoundException("Lock not found with serial: " + id));

	}


	/**
	 * Register a new link in database
	 * Path: /api/lock-gateway-links
	 * Method: POST
	 *
	 * @param link Lock gateway link Object
	 * @return Lock gateway link Object
	 */
	@PostMapping
	public LockGatewayLink createLink(@Valid @RequestBody LockGatewayLink link) {
		return this.lockGatewayLinkService.createLink(link);
	}


	/**
	 * Update existing locl gateway link
	 * Path: /api/lock-gateway-links/{lockSerial}/{gatewaySerial}
	 * Method: PUT
	 *
	 * @param lockSerial    Lock Serial ID
	 * @param gatewaySerial Gateway Serial ID
	 * @param linkDetails   Link details object
	 * @return Lock gateway link updated
	 */
	@PutMapping("/{lockSerial}/{gatewaySerial}")
	public ResponseEntity<LockGatewayLink> updateLink(@PathVariable String lockSerial,
	                                                  @PathVariable String gatewaySerial,
	                                                  @Valid @RequestBody LockGatewayLink linkDetails) {
		LockGatewayLink updatedLink =
				this.lockGatewayLinkService.updateLink(new LockGatewayLinkIdDto(lockSerial, gatewaySerial), linkDetails);
		return ResponseEntity.ok(updatedLink);
	}


	/**
	 * Delete gateway by Serial ID
	 * Path: /api/lock-gateway-links/{lockSerial}/{gatewaySerial}
	 * Method: DELETE
	 *
	 * @param lockSerial    Lock Serial ID
	 * @param gatewaySerial Gateway Serial ID
	 * @return Return Status code 202 if success
	 */
	@DeleteMapping("/{lockSerial}/{gatewaySerial}")
	public ResponseEntity<?> deleteLink(@PathVariable String lockSerial,
	                                    @PathVariable String gatewaySerial) {

		LockGatewayLinkIdDto id = new LockGatewayLinkIdDto(lockSerial, gatewaySerial);

		if (!lockGatewayLinkService.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		lockGatewayLinkService.deleteLink(id);
		return ResponseEntity.ok().build();
	}
} 