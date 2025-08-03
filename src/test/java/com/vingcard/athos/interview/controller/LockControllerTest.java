package com.vingcard.athos.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vingcard.athos.interview.exception.GlobalExceptionHandler;
import com.vingcard.athos.interview.exception.ResourceNotFoundException;
import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.service.LockService;
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
class LockControllerTest {

	private MockMvc mockMvc;

	@Mock
	private LockService lockService;

	@InjectMocks
	private LockController lockController;

	private ObjectMapper objectMapper;

	private Lock testLock;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(lockController)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
		testLock = new Lock("LOCKSERIAL000001", "Main Door", "11:22:33:44:55:66", true, "v2.1.0");
	}

	@Test
	void getAllLocks_ShouldReturnAllLocks() throws Exception {
		Lock lock1 = new Lock("LOCKSERIAL000001", "Main Door", "11:22:33:44:55:66", true, "v2.1.0");
		Lock lock2 = new Lock("LOCKSERIAL000002", "Back Door", "22:33:44:55:66:77", false, "v2.0.0");

		when(lockService.getAllLocks()).thenReturn(Arrays.asList(lock1, lock2));

		mockMvc.perform(get("/api/locks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].serial").value("LOCKSERIAL000001"))
				.andExpect(jsonPath("$[0].name").value("Main Door"))
				.andExpect(jsonPath("$[0].macAddress").value("11:22:33:44:55:66"))
				.andExpect(jsonPath("$[0].online").value(true))
				.andExpect(jsonPath("$[0].version").value("v2.1.0"))
				.andExpect(jsonPath("$[1].serial").value("LOCKSERIAL000002"))
				.andExpect(jsonPath("$[1].name").value("Back Door"))
				.andExpect(jsonPath("$[1].macAddress").value("22:33:44:55:66:77"))
				.andExpect(jsonPath("$[1].online").value(false))
				.andExpect(jsonPath("$[1].version").value("v2.0.0"));
	}

	@Test
	void getLockBySerial_WhenExists_ShouldReturnLock() throws Exception {
		when(lockService.getLockBySerial("LOCKSERIAL000001")).thenReturn(testLock);

		mockMvc.perform(get("/api/locks/LOCKSERIAL000001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.serial").value("LOCKSERIAL000001"))
				.andExpect(jsonPath("$.name").value("Main Door"))
				.andExpect(jsonPath("$.macAddress").value("11:22:33:44:55:66"))
				.andExpect(jsonPath("$.online").value(true))
				.andExpect(jsonPath("$.version").value("v2.1.0"));
	}

	@Test
	void getLockBySerial_WhenNotExists_ShouldReturn404() throws Exception {
		when(lockService.getLockBySerial("nonexistent")).thenReturn(null);

		mockMvc.perform(get("/api/locks/nonexistent"))
				.andExpect(status().isNotFound());
	}

	@Test
	void createLock_WithValidData_ShouldCreateAndReturnLock() throws Exception {
		Lock newLock = new Lock("LOCKSERIAL000003", "Side Door", "33:44:55:66:77:88", false, "v2.2.0");
		when(lockService.createLock(any(Lock.class))).thenReturn(newLock);

		mockMvc.perform(post("/api/locks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newLock)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.serial").value("LOCKSERIAL000003"))
				.andExpect(jsonPath("$.name").value("Side Door"))
				.andExpect(jsonPath("$.macAddress").value("33:44:55:66:77:88"))
				.andExpect(jsonPath("$.online").value(false))
				.andExpect(jsonPath("$.version").value("v2.2.0"));

		verify(lockService).createLock(any(Lock.class));
	}

	@Test
	void createLock_WithEmptySerial_ShouldReturn400() throws Exception {
		Lock invalidLock = new Lock("", "Side Door", "33:44:55:66:77:88", false, "v2.2.0");

		mockMvc.perform(post("/api/locks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidLock)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createLock_WithNullSerial_ShouldReturn400() throws Exception {
		Lock invalidLock = new Lock(null, "Side Door", "33:44:55:66:77:88", false, "v2.2.0");

		mockMvc.perform(post("/api/locks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidLock)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createLock_WithNullVersion_ShouldSetDefaultValues() throws Exception {
		Lock lockWithNullVersion = new Lock();
		lockWithNullVersion.setSerial("LOCKSERIAL000004");
		lockWithNullVersion.setName("Garage Door");
		lockWithNullVersion.setMacAddress("44:55:66:77:88:99");
		lockWithNullVersion.setVersion(null);

		Lock savedLock = new Lock("LOCKSERIAL000004", "Garage Door", "44:55:66:77:88:99", false, "");
		when(lockService.createLock(any(Lock.class))).thenReturn(savedLock);

		mockMvc.perform(post("/api/locks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(lockWithNullVersion)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.version").value(""))
				.andExpect(jsonPath("$.online").value(false));
	}

	@Test
	void updateLock_WhenExists_ShouldUpdateAndReturnLock() throws Exception {
		Lock updatedLock = new Lock("LOCKSERIAL000001", "Updated Main Door", "FF:EE:DD:CC:BB:AA", false, "v3.0.0");

		when(lockService.updateLock(eq("LOCKSERIAL000001"), any(Lock.class))).thenReturn(updatedLock);

		mockMvc.perform(put("/api/locks/LOCKSERIAL000001")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedLock)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated Main Door"))
				.andExpect(jsonPath("$.macAddress").value("FF:EE:DD:CC:BB:AA"))
				.andExpect(jsonPath("$.version").value("v3.0.0"));
	}

	@Test
	void updateLock_WhenNotExists_ShouldReturn404() throws Exception {
		when(lockService.updateLock(eq("nonexistent"), any(Lock.class)))
				.thenThrow(new ResourceNotFoundException("Lock não encontrado"));

		mockMvc.perform(put("/api/locks/nonexistent")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testLock)))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteLock_WhenExists_ShouldDeleteAndReturn200() throws Exception {
		when(lockService.existsById("LOCKSERIAL000001")).thenReturn(true);
		doNothing().when(lockService).deleteLock("LOCKSERIAL000001");

		mockMvc.perform(delete("/api/locks/LOCKSERIAL000001"))
				.andExpect(status().isOk());

		verify(lockService).deleteLock("LOCKSERIAL000001");
	}

	@Test
	void deleteLock_WhenNotExists_ShouldReturn404() throws Exception {
		when(lockService.existsById("nonexistent")).thenReturn(false);

		mockMvc.perform(delete("/api/locks/nonexistent"))
				.andExpect(status().isNotFound());

		verify(lockService, never()).deleteLock(any());
	}
} 