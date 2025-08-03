package com.vingcard.athos.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vingcard.athos.interview.exception.GlobalExceptionHandler;
import com.vingcard.athos.interview.model.dto.LockGatewayLinkIdDto;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.service.LockGatewayLinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LockGatewayLinkControllerTest {

	private MockMvc mockMvc;

	@Mock
	private LockGatewayLinkService lockGatewayLinkService;

	@InjectMocks
	private LockGatewayLinkController lockGatewayLinkController;

	private ObjectMapper objectMapper;

	private LockGatewayLink testLink;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(lockGatewayLinkController)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
		testLink = new LockGatewayLink("LOCKSERIAL000001", "GATEWAYSERIAL0001", -55.5);
	}

	@Test
	void getAllLinks_ShouldReturnAllLinks() throws Exception {
		LockGatewayLink link1 = new LockGatewayLink("LOCKSERIAL000001", "GATEWAYSERIAL0001", -55.5);
		LockGatewayLink link2 = new LockGatewayLink("LOCKSERIAL000002", "GATEWAYSERIAL0001", -60.2);

		when(lockGatewayLinkService.getAllLinks()).thenReturn(Arrays.asList(link1, link2));

		mockMvc.perform(get("/api/lock-gateway-links"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].lockSerial").value("LOCKSERIAL000001"))
				.andExpect(jsonPath("$[0].gatewaySerial").value("GATEWAYSERIAL0001"))
				.andExpect(jsonPath("$[0].rssi").value(-55.5))
				.andExpect(jsonPath("$[1].lockSerial").value("LOCKSERIAL000002"))
				.andExpect(jsonPath("$[1].gatewaySerial").value("GATEWAYSERIAL0001"))
				.andExpect(jsonPath("$[1].rssi").value(-60.2));
	}

	@Test
	void getLinksByLockSerial_ShouldReturnLinksForLock() throws Exception {
		LockGatewayLink link1 = new LockGatewayLink("LOCKSERIAL000001", "GATEWAYSERIAL0001", -55.5);
		LockGatewayLink link2 = new LockGatewayLink("LOCKSERIAL000001", "GATEWAYSERIAL0002", -45.8);

		when(lockGatewayLinkService.getLinksByLockSerial("LOCKSERIAL000001")).thenReturn(Arrays.asList(link1, link2));

		mockMvc.perform(get("/api/lock-gateway-links/lock/LOCKSERIAL000001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].lockSerial").value("LOCKSERIAL000001"))
				.andExpect(jsonPath("$[0].gatewaySerial").value("GATEWAYSERIAL0001"))
				.andExpect(jsonPath("$[0].rssi").value(-55.5))
				.andExpect(jsonPath("$[1].lockSerial").value("LOCKSERIAL000001"))
				.andExpect(jsonPath("$[1].gatewaySerial").value("GATEWAYSERIAL0002"))
				.andExpect(jsonPath("$[1].rssi").value(-45.8));
	}

	@Test
	void getLinksByGatewaySerial_ShouldReturnLinksForGateway() throws Exception {
		LockGatewayLink link1 = new LockGatewayLink("LOCKSERIAL000001", "GATEWAYSERIAL0001", -55.5);
		LockGatewayLink link2 = new LockGatewayLink("LOCKSERIAL000002", "GATEWAYSERIAL0001", -60.2);

		when(lockGatewayLinkService.getLinksByGatewaySerial("GATEWAYSERIAL0001")).thenReturn(Arrays.asList(link1, link2));

		mockMvc.perform(get("/api/lock-gateway-links/gateway/GATEWAYSERIAL0001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].lockSerial").value("LOCKSERIAL000001"))
				.andExpect(jsonPath("$[0].gatewaySerial").value("GATEWAYSERIAL0001"))
				.andExpect(jsonPath("$[0].rssi").value(-55.5))
				.andExpect(jsonPath("$[1].lockSerial").value("LOCKSERIAL000002"))
				.andExpect(jsonPath("$[1].gatewaySerial").value("GATEWAYSERIAL0001"))
				.andExpect(jsonPath("$[1].rssi").value(-60.2));
	}

	@Test
	void getLink_WhenExists_ShouldReturnLink() throws Exception {
		LockGatewayLinkIdDto id = new LockGatewayLinkIdDto("LOCKSERIAL000001", "GATEWAYSERIAL0001");
		when(lockGatewayLinkService.findById(id)).thenReturn(Optional.ofNullable(testLink));

		mockMvc.perform(get("/api/lock-gateway-links/LOCKSERIAL000001/GATEWAYSERIAL0001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lockSerial").value("LOCKSERIAL000001"))
				.andExpect(jsonPath("$.gatewaySerial").value("GATEWAYSERIAL0001"))
				.andExpect(jsonPath("$.rssi").value(-55.5));
	}

	@Test
	void getLink_WhenNotExists_ShouldReturn404() throws Exception {
		LockGatewayLinkIdDto id = new LockGatewayLinkIdDto("nonexistent", "nonexistent");
		when(lockGatewayLinkService.findById(id)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/lock-gateway-links/nonexistent/nonexistent"))
				.andExpect(status().isNotFound());
	}

	@Test
	void createLink_WithValidData_ShouldCreateAndReturnLink() throws Exception {
		LockGatewayLink newLink = new LockGatewayLink("LOCKSERIAL000003", "GATEWAYSERIAL0002", -65.0);
		when(lockGatewayLinkService.createLink(any(LockGatewayLink.class))).thenReturn(newLink);

		mockMvc.perform(post("/api/lock-gateway-links")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newLink)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lockSerial").value("LOCKSERIAL000003"))
				.andExpect(jsonPath("$.gatewaySerial").value("GATEWAYSERIAL0002"))
				.andExpect(jsonPath("$.rssi").value(-65.0));

		verify(lockGatewayLinkService).createLink(any(LockGatewayLink.class));
	}

	@Test
	void createLink_WithEmptyLockSerial_ShouldReturn400() throws Exception {
		LockGatewayLink invalidLink = new LockGatewayLink("", "GATEWAYSERIAL0002", -65.0);

		mockMvc.perform(post("/api/lock-gateway-links")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidLink)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createLink_WithEmptyGatewaySerial_ShouldReturn400() throws Exception {
		LockGatewayLink invalidLink = new LockGatewayLink("LOCKSERIAL000003", "", -65.0);

		mockMvc.perform(post("/api/lock-gateway-links")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidLink)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createLink_WithNullLockSerial_ShouldReturn400() throws Exception {
		LockGatewayLink invalidLink = new LockGatewayLink(null, "GATEWAYSERIAL0002", -65.0);

		mockMvc.perform(post("/api/lock-gateway-links")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidLink)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createLink_WithNullGatewaySerial_ShouldReturn400() throws Exception {
		LockGatewayLink invalidLink = new LockGatewayLink("LOCKSERIAL000003", null, -65.0);

		mockMvc.perform(post("/api/lock-gateway-links")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidLink)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateLink_WhenExists_ShouldUpdateAndReturnLink() throws Exception {
		LockGatewayLinkIdDto id = new LockGatewayLinkIdDto("LOCKSERIAL000001", "GATEWAYSERIAL0001");
		LockGatewayLink updatedLink = new LockGatewayLink("LOCKSERIAL000001", "GATEWAYSERIAL0001", -45.0);

		when(lockGatewayLinkService.updateLink(eq(id), any(LockGatewayLink.class)))
				.thenReturn(updatedLink);

		mockMvc.perform(put("/api/lock-gateway-links/LOCKSERIAL000001/GATEWAYSERIAL0001")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedLink)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.rssi").value(-45.0));
	}

	@Test
	void updateLink_WhenNotExists_ShouldReturn404() throws Exception {
		LockGatewayLinkIdDto id = new LockGatewayLinkIdDto("nonexistent", "nonexistent");
		when(lockGatewayLinkService.updateLink(eq(id), any(LockGatewayLink.class)))
				.thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found"));

		mockMvc.perform(put("/api/lock-gateway-links/nonexistent/nonexistent")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testLink)))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteLink_WhenExists_ShouldDeleteAndReturn200() throws Exception {
		LockGatewayLinkIdDto id = new LockGatewayLinkIdDto("LOCKSERIAL000001", "GATEWAYSERIAL0001");
		when(lockGatewayLinkService.existsById(id)).thenReturn(true);
		doNothing().when(lockGatewayLinkService).deleteLink(id);

		mockMvc.perform(delete("/api/lock-gateway-links/LOCKSERIAL000001/GATEWAYSERIAL0001"))
				.andExpect(status().isOk());

		verify(lockGatewayLinkService).deleteLink(id);
	}

	@Test
	void deleteLink_WhenNotExists_ShouldReturn404() throws Exception {
		LockGatewayLinkIdDto id = new LockGatewayLinkIdDto("nonexistent", "nonexistent");
		when(lockGatewayLinkService.existsById(id)).thenReturn(false);

		mockMvc.perform(delete("/api/lock-gateway-links/nonexistent/nonexistent"))
				.andExpect(status().isNotFound());

		verify(lockGatewayLinkService, never()).deleteLink(any());
	}
} 