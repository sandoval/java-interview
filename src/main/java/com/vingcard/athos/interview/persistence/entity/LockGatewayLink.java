package com.vingcard.athos.interview.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lock_gateway_link")
@IdClass(LockGatewayLinkId.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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

}
