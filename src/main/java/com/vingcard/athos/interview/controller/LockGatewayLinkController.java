package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.service.impl.LockGatewayLinkServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lock-gateway-links")
public class LockGatewayLinkController {

	private final LockGatewayLinkServiceImpl lockGatewayLinkService;

	@Autowired
	public LockGatewayLinkController(LockGatewayLinkServiceImpl lockGatewayLinkService) {
		this.lockGatewayLinkService = lockGatewayLinkService;
	}

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

	@GetMapping("/{lockSerial}/{gatewaySerial}")
	public ResponseEntity<LockGatewayLink> getLink(@PathVariable String lockSerial,
	                                               @PathVariable String gatewaySerial) {
		return this.lockGatewayLinkService.getLink(lockSerial, gatewaySerial);
	}

	@PostMapping
	public LockGatewayLink createLink(@Valid @RequestBody LockGatewayLink link) {
		return this.lockGatewayLinkService.createLink(link);
	}

	@PutMapping("/{lockSerial}/{gatewaySerial}")
	public ResponseEntity<LockGatewayLink> updateLink(@PathVariable String lockSerial,
	                                                  @PathVariable String gatewaySerial,
	                                                  @Valid @RequestBody LockGatewayLink linkDetails) {
		return this.lockGatewayLinkService.updateLink(lockSerial, gatewaySerial, linkDetails);
	}

	@DeleteMapping("/{lockSerial}/{gatewaySerial}")
	public ResponseEntity<?> deleteLink(@PathVariable String lockSerial,
	                                    @PathVariable String gatewaySerial) {
		return this.lockGatewayLinkService.deleteLink(lockSerial, gatewaySerial);
	}
} 