package com.vingcard.athos.interview.persistence.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LockGatewayLinkId implements Serializable {

	private String lockSerial;
	private String gatewaySerial;

}
