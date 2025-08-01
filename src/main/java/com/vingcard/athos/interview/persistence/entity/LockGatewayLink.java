package com.vingcard.athos.interview.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lock_gateway_link")
@IdClass(LockGatewayLinkId.class)
public class LockGatewayLink {
	@Id
	@Column(length = 16, nullable = false)
	private String lockSerial;

	@Id
	@Column(length = 16, nullable = false)
	private String gatewaySerial;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lockSerial", referencedColumnName = "serial", insertable = false, updatable = false)
	private Lock lock;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gatewaySerial", referencedColumnName = "serial", insertable = false, updatable = false)
	private Gateway gateway;

	@Column(nullable = false)
	private double rssi;

	public LockGatewayLink() {
	}

	public LockGatewayLink(String lockSerial, String gatewaySerial, double rssi) {
		this.lockSerial = lockSerial;
		this.gatewaySerial = gatewaySerial;
		this.rssi = rssi;
	}

	public String getLockSerial() {
		return lockSerial;
	}

	public void setLockSerial(String lockSerial) {
		this.lockSerial = lockSerial;
	}

	public String getGatewaySerial() {
		return gatewaySerial;
	}

	public void setGatewaySerial(String gatewaySerial) {
		this.gatewaySerial = gatewaySerial;
	}

	public Lock getLock() {
		return lock;
	}

	public void setLock(Lock lock) {
		this.lock = lock;
	}

	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	public double getRssi() {
		return rssi;
	}

	public void setRssi(double rssi) {
		this.rssi = rssi;
	}
}
