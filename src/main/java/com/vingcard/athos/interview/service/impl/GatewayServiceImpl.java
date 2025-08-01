package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.exception.NotFoundExceptionResponse;
import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
import com.vingcard.athos.interview.service.GatewayService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GatewayServiceImpl implements GatewayService {

	private final GatewayRepository gatewayRepository;


	/**
	 * Return all gateways from database
	 *
	 * @return List with all gateways from database
	 */
	@Override
	public List<Gateway> getAllGateways() {
		return gatewayRepository.findAll();
	}


	/**
	 * Filter gateway by serial ID
	 *
	 * @param serial Gateway serial ID
	 * @return Created gateway object
	 */
	@Override
	public ResponseEntity<Gateway> getGatewayBySerial(String serial) {
		Optional<Gateway> gateway = gatewayRepository.findById(serial);
		return gateway.map(ResponseEntity::ok)
				.orElseThrow(() -> new NotFoundExceptionResponse("Lock not found with serial: " + serial));
	}


	/**
	 * Add new Gateway in database
	 *
	 * @param gateway Gateway object
	 * @return Gateway object newly created
	 */
	@Override
	public Gateway createGateway(Gateway gateway) {
		if (gateway.getSerial() == null || gateway.getSerial().trim().isEmpty()) {
			throw new IllegalArgumentException("Serial cannot be empty");
		}

		// Set default values for new gateways
		if (gateway.getVersion() == null) {
			gateway.setVersion("");
		}

		gateway.setOnline(false); // New gateways start offline
		return gatewayRepository.save(gateway);
	}


	/**
	 * Update existing gateway from database filtered by Gateway serial ID
	 *
	 * @param serial         Gateway serial ID
	 * @param gatewayDetails Gateway object with new values
	 * @return Updated gateway object
	 */
	@Override
	public ResponseEntity<Gateway> updateGateway(String serial, Gateway gatewayDetails) {
		Optional<Gateway> optionalGateway = gatewayRepository.findById(serial);
		if (optionalGateway.isPresent()) {
			Gateway gateway = optionalGateway.get();
			gateway.setMacAddress(gatewayDetails.getMacAddress());
			gateway.setVersion(gatewayDetails.getVersion());

			// Only update online status if explicitly provided
			if (gatewayDetails.isOnline() != gateway.isOnline()) {
				gateway.setOnline(gatewayDetails.isOnline());
			}

			return ResponseEntity.ok(gatewayRepository.save(gateway));
		} else {
			throw new NotFoundExceptionResponse("Lock not found with serial: " + serial);
		}
	}


	/**
	 * Delete Gateway from Database
	 *
	 * @param serial Gateway Serial ID
	 * @return Is deleted response status
	 */
	@Override
	public ResponseEntity<?> deleteGateway(String serial) {
		if (gatewayRepository.existsById(serial)) {
			gatewayRepository.deleteById(serial);
			return ResponseEntity.ok().build();
		} else {
			throw new NotFoundExceptionResponse("Lock not found with serial: " + serial);
		}
	}
}
