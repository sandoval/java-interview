package com.vingcard.athos.interview.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class GatewayIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        gatewayRepository.deleteAll();
    }

    @Test
    void testCreateAndRetrieveGateway() throws Exception {
        // Create a gateway
        Gateway gateway = new Gateway("GATEWAY00000001", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");
        
        mockMvc.perform(post("/docs/http-tests/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serial").value("GATEWAY00000001"))
                .andExpect(jsonPath("$.macAddress").value("AA:BB:CC:DD:EE:FF"))
                .andExpect(jsonPath("$.online").value(false))
                .andExpect(jsonPath("$.version").value("v1.0.0"));

        // Verify it was saved to database
        assertTrue(gatewayRepository.findById("GATEWAY00000001").isPresent());

        // Retrieve the gateway
        mockMvc.perform(get("/docs/http-tests/gateways/GATEWAY00000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serial").value("GATEWAY00000001"))
                .andExpect(jsonPath("$.macAddress").value("AA:BB:CC:DD:EE:FF"))
                .andExpect(jsonPath("$.online").value(false))
                .andExpect(jsonPath("$.version").value("v1.0.0"));
    }

    @Test
    void testCreateMultipleGatewaysAndListAll() throws Exception {
        // Create multiple gateways
        Gateway gateway1 = new Gateway("GATEWAY00000001", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");
        Gateway gateway2 = new Gateway("GATEWAY00000002", "BB:CC:DD:EE:FF:00", false, "v1.1.0");
        Gateway gateway3 = new Gateway("GATEWAY00000003", "CC:DD:EE:FF:00:11", true, "v2.0.0");

        mockMvc.perform(post("/docs/http-tests/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/docs/http-tests/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway2)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/docs/http-tests/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway3)))
                .andExpect(status().isOk());

        // Verify all gateways are in database
        assertEquals(3, gatewayRepository.count());

        // List all gateways
        mockMvc.perform(get("/docs/http-tests/gateways"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].serial").value("GATEWAY00000001"))
                .andExpect(jsonPath("$[1].serial").value("GATEWAY00000002"))
                .andExpect(jsonPath("$[2].serial").value("GATEWAY00000003"));
    }

    @Test
    void testUpdateGateway() throws Exception {
        // Create a gateway
        Gateway gateway = new Gateway("GATEWAY00000001", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");
        mockMvc.perform(post("/docs/http-tests/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway)))
                .andExpect(status().isOk());

        // Update the gateway
        Gateway updatedGateway = new Gateway("GATEWAY00000001", "FF:EE:DD:CC:BB:AA", false, "v2.0.0");
        mockMvc.perform(put("/docs/http-tests/gateways/GATEWAY00000001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedGateway)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.macAddress").value("FF:EE:DD:CC:BB:AA"))
                .andExpect(jsonPath("$.online").value(false))
                .andExpect(jsonPath("$.version").value("v2.0.0"));

        // Verify the update in database
        Gateway savedGateway = gatewayRepository.findById("GATEWAY00000001").orElseThrow();
        assertEquals("FF:EE:DD:CC:BB:AA", savedGateway.getMacAddress());
        assertFalse(savedGateway.isOnline());
        assertEquals("v2.0.0", savedGateway.getVersion());
    }

    @Test
    void testDeleteGateway() throws Exception {
        // Create a gateway
        Gateway gateway = new Gateway("GATEWAY00000001", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");
        mockMvc.perform(post("/docs/http-tests/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway)))
                .andExpect(status().isOk());

        // Verify it exists
        assertTrue(gatewayRepository.findById("GATEWAY00000001").isPresent());

        // Delete the gateway
        mockMvc.perform(delete("/docs/http-tests/gateways/GATEWAY00000001"))
                .andExpect(status().isOk());

        // Verify it was deleted
        assertFalse(gatewayRepository.findById("GATEWAY00000001").isPresent());

        // Try to retrieve deleted gateway
        mockMvc.perform(get("/docs/http-tests/gateways/GATEWAY00000001"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateGatewayWithDefaultValues() throws Exception {
        // Create a gateway with null version
        Gateway gateway = new Gateway();
        gateway.setSerial("GATEWAY00000001");
        gateway.setMacAddress("AA:BB:CC:DD:EE:FF");
        gateway.setVersion(null);

        mockMvc.perform(post("/docs/http-tests/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(""))
                .andExpect(jsonPath("$.online").value(false));

        // Verify default values in database
        Gateway savedGateway = gatewayRepository.findById("GATEWAY00000001").orElseThrow();
        assertEquals("", savedGateway.getVersion());
        assertFalse(savedGateway.isOnline());
    }

    @Test
    void testCreateGatewayWithEmptySerial() throws Exception {
        Gateway gateway = new Gateway("", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");

        mockMvc.perform(post("/docs/http-tests/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateNonExistentGateway() throws Exception {
        Gateway gateway = new Gateway("nonexistent", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");

        mockMvc.perform(put("/docs/http-tests/gateways/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteNonExistentGateway() throws Exception {
        mockMvc.perform(delete("/docs/http-tests/gateways/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetNonExistentGateway() throws Exception {
        mockMvc.perform(get("/docs/http-tests/gateways/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCorsHeaders() throws Exception {
        mockMvc.perform(get("/docs/http-tests/gateways")
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }
} 