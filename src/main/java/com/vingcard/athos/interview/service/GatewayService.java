package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.persistence.entity.Gateway;

import java.util.List;

public interface GatewayService {

	List<Gateway> getAllGateways();

	Gateway getGatewayBySerial(String serial);

	Gateway createGateway(Gateway gateway);

	Gateway updateGateway(String serial, Gateway gatewayDetails);

	void deleteGateway(String serial);

	boolean existsById(String serial);
}
