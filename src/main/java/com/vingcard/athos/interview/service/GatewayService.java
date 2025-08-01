package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.persistence.entity.Gateway;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GatewayService {

	List<Gateway> getAllGateways();

	ResponseEntity<Gateway> getGatewayBySerial(String serial);

	Gateway createGateway(Gateway gateway);

	ResponseEntity<Gateway> updateGateway(String serial, Gateway gatewayDetails);

	ResponseEntity<?> deleteGateway(String serial);

}
