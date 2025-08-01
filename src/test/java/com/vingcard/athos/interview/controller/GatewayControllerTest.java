package com.vingcard.athos.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vingcard.athos.interview.exception.GlobalExceptionHandler;
import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GatewayControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GatewayRepository gatewayRepository;

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
        
        when(gatewayRepository.findAll()).thenReturn(Arrays.asList(gateway1, gateway2));

        mockMvc.perform(get("/http/gateways"))
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
        when(gatewayRepository.findById("GATEWAY00000001")).thenReturn(Optional.of(testGateway));

        mockMvc.perform(get("/http/gateways/GATEWAY00000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serial").value("GATEWAY00000001"))
                .andExpect(jsonPath("$.macAddress").value("AA:BB:CC:DD:EE:FF"))
                .andExpect(jsonPath("$.online").value(true))
                .andExpect(jsonPath("$.version").value("v1.0.0"));
    }

    @Test
    void getGatewayBySerial_WhenNotExists_ShouldReturn404() throws Exception {
        when(gatewayRepository.findById("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/http/gateways/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createGateway_WithValidData_ShouldCreateAndReturnGateway() throws Exception {
        Gateway newGateway = new Gateway("GATEWAY00000003", "CC:DD:EE:FF:00:11", false, "v2.0.0");
        when(gatewayRepository.save(any(Gateway.class))).thenReturn(newGateway);

        mockMvc.perform(post("/http/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newGateway)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serial").value("GATEWAY00000003"))
                .andExpect(jsonPath("$.macAddress").value("CC:DD:EE:FF:00:11"))
                .andExpect(jsonPath("$.online").value(false))
                .andExpect(jsonPath("$.version").value("v2.0.0"));

        verify(gatewayRepository).save(any(Gateway.class));
    }

    @Test
    void createGateway_WithEmptySerial_ShouldReturn400() throws Exception {
        Gateway invalidGateway = new Gateway("", "CC:DD:EE:FF:00:11", false, "v2.0.0");

        mockMvc.perform(post("/http/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidGateway)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createGateway_WithNullSerial_ShouldReturn400() throws Exception {
        Gateway invalidGateway = new Gateway(null, "CC:DD:EE:FF:00:11", false, "v2.0.0");

        mockMvc.perform(post("/http/gateways")
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

        Gateway savedGateway = new Gateway("GATEWAY00000004", "DD:EE:FF:00:11:22", false, "");
        when(gatewayRepository.save(any(Gateway.class))).thenReturn(savedGateway);

        mockMvc.perform(post("/http/gateways")
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

        when(gatewayRepository.findById("GATEWAY00000001")).thenReturn(Optional.of(existingGateway));
        when(gatewayRepository.save(any(Gateway.class))).thenReturn(updatedGateway);

        mockMvc.perform(put("/http/gateways/GATEWAY00000001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedGateway)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.macAddress").value("FF:EE:DD:CC:BB:AA"))
                .andExpect(jsonPath("$.version").value("v2.0.0"));
    }

    @Test
    void updateGateway_WhenNotExists_ShouldReturn404() throws Exception {
        when(gatewayRepository.findById("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(put("/http/gateways/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testGateway)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteGateway_WhenExists_ShouldDeleteAndReturn200() throws Exception {
        when(gatewayRepository.existsById("GATEWAY00000001")).thenReturn(true);
        doNothing().when(gatewayRepository).deleteById("GATEWAY00000001");

        mockMvc.perform(delete("/http/gateways/GATEWAY00000001"))
                .andExpect(status().isOk());

        verify(gatewayRepository).deleteById("GATEWAY00000001");
    }

    @Test
    void deleteGateway_WhenNotExists_ShouldReturn404() throws Exception {
        when(gatewayRepository.existsById("nonexistent")).thenReturn(false);

        mockMvc.perform(delete("/http/gateways/nonexistent"))
                .andExpect(status().isNotFound());

        verify(gatewayRepository, never()).deleteById(any());
    }
} 