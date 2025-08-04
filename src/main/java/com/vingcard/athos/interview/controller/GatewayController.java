package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.exception.ResourceNotFoundException;
import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.service.GatewayService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gateways")
@AllArgsConstructor
public class GatewayController {

	private final GatewayService gatewayService;


	/**
	 * Get All gateways from database
	 * Path: /api/gateways
	 * Method: GET
	 *
	 * @return List of all gateways from database
	 */
	@GetMapping
	public List<Gateway> getAllGateways() {
		return this.gatewayService.getAllGateways();
	}


	/**
	 * Returns gateway from database filtered by Gateway Serial ID
	 * Path: /api/gateways/{serial}
	 * Method: GET
	 *
	 * @param serial Gateway Serial ID
	 * @return Existing gateway Object
	 */
	@GetMapping("/{serial}")
	public ResponseEntity<Gateway> getGatewayBySerial(@PathVariable String serial) {
		Gateway lock = gatewayService.getGatewayBySerial(serial);
		if (lock == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lock);
	}


	/**
	 * Add a new gateway to Database
	 * Path: /api/gateways
	 * Method: POST
	 *
	 * @param gateway Gateway Object
	 * @return Gateway newly created
	 */
	@PostMapping
	public Gateway createGateway(@Valid @RequestBody Gateway gateway) {
		return this.gatewayService.createGateway(gateway);
	}


	/**
	 * Update gateway filtered by Gateway Serial ID from database
	 * Path: /api/gateways/{serial}
	 * Method: PUT
	 *
	 * @param serial         Gateway Serial ID
	 * @param gatewayDetails Gateway Object
	 * @return Gateway newly updated
	 */
	@PutMapping("/{serial}")
	public ResponseEntity<Gateway> updateGateway(@PathVariable String serial,
	                                             @Valid @RequestBody Gateway gatewayDetails) {
		Gateway existing = gatewayService.getGatewayBySerial(serial);
		if (existing == null) {
			return ResponseEntity.notFound().build();
		}

		Gateway updated = gatewayService.updateGateway(serial, gatewayDetails);
		return ResponseEntity.ok(updated);
	}


	/**
	 * Delete Gateway filtered by Gateway Serial ID from database
	 * Path: /api/gateways/{serial}
	 * Method: DELETE
	 *
	 * @param serial Gateway Serial ID
	 * @return Return Status code 202 if success
	 */
	@DeleteMapping("/{serial}")
	public ResponseEntity<?> deleteGateway(@PathVariable String serial) {
		if (!gatewayService.existsById(serial)) {
			throw new ResourceNotFoundException("Gateway not found");
		}

		gatewayService.deleteGateway(serial);
		return ResponseEntity.ok().build();
	}
} 