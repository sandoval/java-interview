package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.service.impl.GatewayServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gateways")
public class GatewayController {

	private final GatewayServiceImpl gatewayService;

	@Autowired
	public GatewayController(GatewayServiceImpl gatewayService) {
		this.gatewayService = gatewayService;
	}

	@GetMapping
	public List<Gateway> getAllGateways() {
		return this.gatewayService.getAllGateways();
	}

	@GetMapping("/{serial}")
	public ResponseEntity<Gateway> getGatewayBySerial(@PathVariable String serial) {
		return this.gatewayService.getGatewayBySerial(serial);
	}

	@PostMapping
	public Gateway createGateway(@Valid @RequestBody Gateway gateway) {
		return this.gatewayService.createGateway(gateway);
	}

	@PutMapping("/{serial}")
	public ResponseEntity<Gateway> updateGateway(@PathVariable String serial, @Valid @RequestBody Gateway gatewayDetails) {
		return this.gatewayService.updateGateway(serial, gatewayDetails);
	}

	@DeleteMapping("/{serial}")
	public ResponseEntity<?> deleteGateway(@PathVariable String serial) {
		return this.gatewayService.deleteGateway(serial);
	}
} 