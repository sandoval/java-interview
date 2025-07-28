package com.vingcard.athos.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLinkId;
import com.vingcard.athos.interview.persistence.repository.LockGatewayLinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LockGatewayLinkController.class)
class LockGatewayLinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LockGatewayLinkRepository linkRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private LockGatewayLink testLink;

    @BeforeEach
    void setUp() {
        testLink = new LockGatewayLink("LOCKSERIAL000001", "GATEWAYSERIAL0001", -55.5);
    }

    @Test
    void getAllLinks_ShouldReturnAllLinks() throws Exception {
        LockGatewayLink link1 = new LockGatewayLink("LOCKSERIAL000001", "GATEWAYSERIAL0001", -55.5);
        LockGatewayLink link2 = new LockGatewayLink("LOCKSERIAL000002", "GATEWAYSERIAL0001", -60.2);
        
        when(linkRepository.findAll()).thenReturn(Arrays.asList(link1, link2));

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
        
        when(linkRepository.findByLockSerial("LOCKSERIAL000001")).thenReturn(Arrays.asList(link1, link2));

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
        
        when(linkRepository.findByGatewaySerial("GATEWAYSERIAL0001")).thenReturn(Arrays.asList(link1, link2));

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
        LockGatewayLinkId id = new LockGatewayLinkId("LOCKSERIAL000001", "GATEWAYSERIAL0001");
        when(linkRepository.findById(id)).thenReturn(Optional.of(testLink));

        mockMvc.perform(get("/api/lock-gateway-links/LOCKSERIAL000001/GATEWAYSERIAL0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lockSerial").value("LOCKSERIAL000001"))
                .andExpect(jsonPath("$.gatewaySerial").value("GATEWAYSERIAL0001"))
                .andExpect(jsonPath("$.rssi").value(-55.5));
    }

    @Test
    void getLink_WhenNotExists_ShouldReturn404() throws Exception {
        LockGatewayLinkId id = new LockGatewayLinkId("nonexistent", "nonexistent");
        when(linkRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/lock-gateway-links/nonexistent/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createLink_WithValidData_ShouldCreateAndReturnLink() throws Exception {
        LockGatewayLink newLink = new LockGatewayLink("LOCKSERIAL000003", "GATEWAYSERIAL0002", -65.0);
        when(linkRepository.save(any(LockGatewayLink.class))).thenReturn(newLink);

        mockMvc.perform(post("/api/lock-gateway-links")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLink)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lockSerial").value("LOCKSERIAL000003"))
                .andExpect(jsonPath("$.gatewaySerial").value("GATEWAYSERIAL0002"))
                .andExpect(jsonPath("$.rssi").value(-65.0));

        verify(linkRepository).save(any(LockGatewayLink.class));
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
        LockGatewayLinkId id = new LockGatewayLinkId("LOCKSERIAL000001", "GATEWAYSERIAL0001");
        LockGatewayLink existingLink = new LockGatewayLink("LOCKSERIAL000001", "GATEWAYSERIAL0001", -55.5);
        LockGatewayLink updatedLink = new LockGatewayLink("LOCKSERIAL000001", "GATEWAYSERIAL0001", -45.0);

        when(linkRepository.findById(id)).thenReturn(Optional.of(existingLink));
        when(linkRepository.save(any(LockGatewayLink.class))).thenReturn(updatedLink);

        mockMvc.perform(put("/api/lock-gateway-links/LOCKSERIAL000001/GATEWAYSERIAL0001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedLink)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rssi").value(-45.0));
    }

    @Test
    void updateLink_WhenNotExists_ShouldReturn404() throws Exception {
        LockGatewayLinkId id = new LockGatewayLinkId("nonexistent", "nonexistent");
        when(linkRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/lock-gateway-links/nonexistent/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLink)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLink_WhenExists_ShouldDeleteAndReturn200() throws Exception {
        LockGatewayLinkId id = new LockGatewayLinkId("LOCKSERIAL000001", "GATEWAYSERIAL0001");
        when(linkRepository.existsById(id)).thenReturn(true);
        doNothing().when(linkRepository).deleteById(id);

        mockMvc.perform(delete("/api/lock-gateway-links/LOCKSERIAL000001/GATEWAYSERIAL0001"))
                .andExpect(status().isOk());

        verify(linkRepository).deleteById(id);
    }

    @Test
    void deleteLink_WhenNotExists_ShouldReturn404() throws Exception {
        LockGatewayLinkId id = new LockGatewayLinkId("nonexistent", "nonexistent");
        when(linkRepository.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/api/lock-gateway-links/nonexistent/nonexistent"))
                .andExpect(status().isNotFound());

        verify(linkRepository, never()).deleteById(any());
    }
} 