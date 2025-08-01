package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.exception.ResourceNotFoundException;
import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
import com.vingcard.athos.interview.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GatewayServiceImpl implements GatewayService {

	private final GatewayRepository gatewayRepository;

	@Autowired
	public GatewayServiceImpl(GatewayRepository gatewayRepository) {
		this.gatewayRepository = gatewayRepository;
	}

	@Override
	public List<Gateway> getAllGateways() {
		return gatewayRepository.findAll();
	}

	@Override
	public ResponseEntity<Gateway> getGatewayBySerial(String serial) {
		Optional<Gateway> gateway = gatewayRepository.findById(serial);
		return gateway.map(ResponseEntity::ok)
				.orElseThrow(() -> new ResourceNotFoundException("Lock not found with serial: " + serial));
	}

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
			throw new ResourceNotFoundException("Lock not found with serial: " + serial);
		}
	}

	@Override
	public ResponseEntity<?> deleteGateway(String serial) {
		if (gatewayRepository.existsById(serial)) {
			gatewayRepository.deleteById(serial);
			return ResponseEntity.ok().build();
		} else {
			throw new ResourceNotFoundException("Lock not found with serial: " + serial);
		}
	}
}
