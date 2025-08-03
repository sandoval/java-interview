package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.exception.ResourceNotFoundException;
import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
import com.vingcard.athos.interview.service.GatewayService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
	public Gateway getGatewayBySerial(String serial) {
		return gatewayRepository.findById(serial)
				.orElseThrow(() -> new ResourceNotFoundException("Gateway not found: " + serial));
	}


	/**
	 * Add new Gateway in database
	 *
	 * @param gateway Gateway object
	 * @return Gateway object newly created
	 */
	@Override
	public Gateway createGateway(@Valid Gateway gateway) {

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
	public Gateway updateGateway(String serial, Gateway gatewayDetails) {
		Optional<Gateway> optionalGateway = this.gatewayRepository.findById(serial);

		if (optionalGateway.isEmpty()) {
			throw new ResourceNotFoundException("Gateway not found");
		}

		optionalGateway.get().setSerial(gatewayDetails.getSerial());
		optionalGateway.get().setMacAddress(gatewayDetails.getMacAddress());
		optionalGateway.get().setVersion(gatewayDetails.getVersion());
		optionalGateway.get().setVersion(gatewayDetails.getVersion());

		// Only update online status if explicitly provided
		if (optionalGateway.get().isOnline() != gatewayDetails.isOnline()) {
			optionalGateway.get().setOnline(gatewayDetails.isOnline());
		}

		return gatewayRepository.save(optionalGateway.get());
	}


	/**
	 * Delete Gateway from Database
	 *
	 * @param serial Gateway Serial ID
	 */
	@Override
	public void deleteGateway(String serial) {
		Gateway lock = gatewayRepository.findById(serial)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gateway not found."));

		gatewayRepository.delete(lock);
	}


	/**
	 * Check if Gateway existis by Serial ID
	 *
	 * @param serial Serial ID
	 * @return boolean
	 */
	@Override
	public boolean existsById(String serial) {
		return gatewayRepository.existsById(serial);
	}
}
