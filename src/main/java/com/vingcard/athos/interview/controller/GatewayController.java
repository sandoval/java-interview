package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gateways")
@CrossOrigin(origins = "http://localhost:3000")
public class GatewayController {

    private final GatewayRepository gatewayRepository;

    @Autowired
    public GatewayController(GatewayRepository gatewayRepository) {
        this.gatewayRepository = gatewayRepository;
    }

    @GetMapping
    public List<Gateway> getAllGateways() {
        return gatewayRepository.findAll();
    }

    @GetMapping("/{serial}")
    public ResponseEntity<Gateway> getGatewayBySerial(@PathVariable String serial) {
        Optional<Gateway> gateway = gatewayRepository.findById(serial);
        return gateway.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Gateway createGateway(@Valid @RequestBody Gateway gateway) {
        if (gateway.getSerial() == null || gateway.getSerial().trim().isEmpty()) {
            throw new IllegalArgumentException("Serial cannot be empty");
        }
        // Set default values for new gateways
        if (gateway.getVersion() == null) {
            gateway.setVersion("");
        }
        gateway.setOnline(false); // New gateways start offline
        return gatewayRepository.save(gateway);
    }

    @PutMapping("/{serial}")
    public ResponseEntity<Gateway> updateGateway(@PathVariable String serial, @Valid @RequestBody Gateway gatewayDetails) {
        Optional<Gateway> optionalGateway = gatewayRepository.findById(serial);
        if (optionalGateway.isPresent()) {
            Gateway gateway = optionalGateway.get();
            gateway.setMacAddress(gatewayDetails.getMacAddress());
            gateway.setVersion(gatewayDetails.getVersion());
            // Only update online status if explicitly provided
            if (gatewayDetails.isOnline() != gateway.isOnline()) {
                gateway.setOnline(gatewayDetails.isOnline());
            }
            return ResponseEntity.ok(gatewayRepository.save(gateway));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{serial}")
    public ResponseEntity<?> deleteGateway(@PathVariable String serial) {
        if (gatewayRepository.existsById(serial)) {
            gatewayRepository.deleteById(serial);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 