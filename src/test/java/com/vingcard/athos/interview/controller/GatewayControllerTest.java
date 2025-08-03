package com.vingcard.athos.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vingcard.athos.interview.exception.GlobalExceptionHandler;
import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.service.GatewayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GatewayControllerTest {

	private MockMvc mockMvc;

	@Mock
	private GatewayService gatewayService;

	@InjectMocks
	private GatewayController gatewayController;

	private ObjectMapper objectMapper;

	private Gateway testGateway;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(gatewayController)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
		testGateway = new Gateway("GATEWAY00000001", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");
	}

	@Test
	void getAllGateways_ShouldReturnAllGateways() throws Exception {
		Gateway gateway1 = new Gateway("GATEWAY00000001", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");
		Gateway gateway2 = new Gateway("GATEWAY00000002", "BB:CC:DD:EE:FF:00", false, "v1.1.0");

		when(gatewayService.getAllGateways()).thenReturn(Arrays.asList(gateway1, gateway2));

		mockMvc.perform(get("/api/gateways"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].serial").value("GATEWAY00000001"))
				.andExpect(jsonPath("$[0].macAddress").value("AA:BB:CC:DD:EE:FF"))
				.andExpect(jsonPath("$[0].online").value(true))
				.andExpect(jsonPath("$[0].version").value("v1.0.0"))
				.andExpect(jsonPath("$[1].serial").value("GATEWAY00000002"))
				.andExpect(jsonPath("$[1].macAddress").value("BB:CC:DD:EE:FF:00"))
				.andExpect(jsonPath("$[1].online").value(false))
				.andExpect(jsonPath("$[1].version").value("v1.1.0"));
	}

	@Test
	void getGatewayBySerial_WhenExists_ShouldReturnGateway() throws Exception {
		when(gatewayService.getGatewayBySerial("GATEWAY00000001")).thenReturn(testGateway);

		mockMvc.perform(get("/api/gateways/GATEWAY00000001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.serial").value("GATEWAY00000001"))
				.andExpect(jsonPath("$.macAddress").value("AA:BB:CC:DD:EE:FF"))
				.andExpect(jsonPath("$.online").value(true))
				.andExpect(jsonPath("$.version").value("v1.0.0"));
	}

	@Test
	void getGatewayBySerial_WhenNotExists_ShouldReturn404() throws Exception {
		when(gatewayService.getGatewayBySerial("nonexistent")).thenReturn(null);

		mockMvc.perform(get("/api/gateways/nonexistent"))
				.andExpect(status().isNotFound());
	}

	@Test
	void createGateway_WithValidData_ShouldCreateAndReturnGateway() throws Exception {
		Gateway newGateway = new Gateway("GATEWAY00000003", "CC:DD:EE:FF:00:11", false, "v2.0.0");
		when(gatewayService.createGateway(any(Gateway.class))).thenReturn(newGateway);

		mockMvc.perform(post("/api/gateways")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newGateway)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.serial").value("GATEWAY00000003"))
				.andExpect(jsonPath("$.macAddress").value("CC:DD:EE:FF:00:11"))
				.andExpect(jsonPath("$.online").value(false))
				.andExpect(jsonPath("$.version").value("v2.0.0"));

		verify(gatewayService).createGateway(any(Gateway.class));
	}

	@Test
	void createGateway_WithEmptySerial_ShouldReturn400() throws Exception {
		Gateway invalidGateway = new Gateway("", "CC:DD:EE:FF:00:11", false, "v2.0.0");

		mockMvc.perform(post("/api/gateways")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidGateway)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createGateway_WithNullSerial_ShouldReturn400() throws Exception {
		Gateway invalidGateway = new Gateway(null, "CC:DD:EE:FF:00:11", false, "v2.0.0");

		mockMvc.perform(post("/api/gateways")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidGateway)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createGateway_WithNullVersion_ShouldSetDefaultValues() throws Exception {
		Gateway gatewayWithNullVersion = new Gateway();
		gatewayWithNullVersion.setSerial("GATEWAY00000004");
		gatewayWithNullVersion.setMacAddress("DD:EE:FF:00:11:22");
		gatewayWithNullVersion.setVersion(null);

		Gateway createGatewaydGateway = new Gateway("GATEWAY00000004", "DD:EE:FF:00:11:22", false, "");
		when(gatewayService.createGateway(any(Gateway.class))).thenReturn(createGatewaydGateway);

		mockMvc.perform(post("/api/gateways")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(gatewayWithNullVersion)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.version").value(""))
				.andExpect(jsonPath("$.online").value(false));
	}

	@Test
	void updateGateway_WhenExists_ShouldUpdateAndReturnGateway() throws Exception {
		Gateway existingGateway = new Gateway("GATEWAY00000001", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");
		Gateway updatedGateway = new Gateway("GATEWAY00000001", "FF:EE:DD:CC:BB:AA", false, "v2.0.0");

		when(gatewayService.getGatewayBySerial("GATEWAY00000001")).thenReturn(existingGateway);
		when(gatewayService.updateGateway(anyString(), any(Gateway.class))).thenReturn(updatedGateway);

		mockMvc.perform(put("/api/gateways/GATEWAY00000001")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedGateway)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.macAddress").value("FF:EE:DD:CC:BB:AA"))
				.andExpect(jsonPath("$.version").value("v2.0.0"));
	}

	@Test
	void updateGateway_WhenNotExists_ShouldReturn404() throws Exception {
		when(gatewayService.getGatewayBySerial("nonexistent")).thenReturn(null);

		mockMvc.perform(put("/api/gateways/nonexistent")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testGateway)))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteGateway_WhenExists_ShouldDeleteAndReturn200() throws Exception {
		when(gatewayService.existsById("GATEWAY00000001")).thenReturn(true);
		doNothing().when(gatewayService).deleteGateway("GATEWAY00000001");

		mockMvc.perform(delete("/api/gateways/GATEWAY00000001"))
				.andExpect(status().isOk());

		verify(gatewayService).deleteGateway("GATEWAY00000001");
	}

	@Test
	void deleteGateway_WhenNotExists_ShouldReturn404() throws Exception {
		mockMvc.perform(delete("/api/gateways/nonexistent"))
				.andExpect(status().isNotFound());

		verify(gatewayService, never()).deleteGateway(any());
	}
} 