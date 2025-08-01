package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.service.impl.LockGatewayLinkServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lock-gateway-links")
@AllArgsConstructor
public class LockGatewayLinkController {

	private final LockGatewayLinkServiceImpl lockGatewayLinkService;

	@GetMapping
	public List<LockGatewayLink> getAllLinks() {
		return this.lockGatewayLinkService.getAllLinks();
	}

	@GetMapping("/lock/{lockSerial}")
	public List<LockGatewayLink> getLinksByLockSerial(@PathVariable String lockSerial) {
		return this.lockGatewayLinkService.getLinksByLockSerial(lockSerial);
	}

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
		return this.lockGatewayLinkService.getLink(lockSerial, gatewaySerial);
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
		return this.lockGatewayLinkService.updateLink(lockSerial, gatewaySerial, linkDetails);
	}


	/**
	 * Delete gateway by Serial ID
	 * Path: /api/lock-gateway-links/{lockSerial}/{gatewaySerial}
	 * Method: DELETE
	 *
	 * @param lockSerial    Lock Serial ID
	 * @param gatewaySerial Gateway Serial ID
	 * @return Return Status code 200 if success
	 */
	@ResponseStatus(HttpStatus.ACCEPTED)
	@DeleteMapping("/{lockSerial}/{gatewaySerial}")
	public ResponseEntity<?> deleteLink(@PathVariable String lockSerial,
	                                    @PathVariable String gatewaySerial) {
		return this.lockGatewayLinkService.deleteLink(lockSerial, gatewaySerial);
	}
} 