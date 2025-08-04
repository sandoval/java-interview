package com.vingcard.athos.interview.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LockGatewayLinkIdDto implements Serializable {

	private String lockSerial;
	private String gatewaySerial;

}
