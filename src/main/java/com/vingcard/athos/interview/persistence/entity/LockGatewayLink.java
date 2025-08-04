package com.vingcard.athos.interview.persistence.entity;

import com.vingcard.athos.interview.model.dto.LockGatewayLinkIdDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "lock_gateway_link")
@IdClass(LockGatewayLinkIdDto.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LockGatewayLink {

	public LockGatewayLink(
			String lockSerial,
			String gatewaySerial,
			double rssi
	) {
		this.lockSerial = lockSerial;
		this.gatewaySerial = gatewaySerial;
		this.rssi = rssi;
	}

	@Id
	@Column(length = 16, nullable = false)
	@NotNull(message = "Lock Serial Cannot be null")
	@NotEmpty(message = "Lock Serial Cannot be Empty")
	private String lockSerial;

	@Id
	@Column(length = 16, nullable = false)
	@NotNull(message = "Gateway Serial Cannot be null")
	@NotEmpty(message = "Gateway Serial Cannot be Empty")
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
