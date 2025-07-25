package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLinkId;
import com.vingcard.athos.interview.persistence.repository.LockGatewayLinkRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lock-gateway-links")
@CrossOrigin(origins = "http://localhost:3000")
public class LockGatewayLinkController {

    private final LockGatewayLinkRepository linkRepository;

    @Autowired
    public LockGatewayLinkController(LockGatewayLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @GetMapping
    public List<LockGatewayLink> getAllLinks() {
        return linkRepository.findAll();
    }

    @GetMapping("/lock/{lockSerial}")
    public List<LockGatewayLink> getLinksByLockSerial(@PathVariable String lockSerial) {
        return linkRepository.findByLockSerial(lockSerial);
    }

    @GetMapping("/gateway/{gatewaySerial}")
    public List<LockGatewayLink> getLinksByGatewaySerial(@PathVariable String gatewaySerial) {
        return linkRepository.findByGatewaySerial(gatewaySerial);
    }

    @GetMapping("/{lockSerial}/{gatewaySerial}")
    public ResponseEntity<LockGatewayLink> getLink(@PathVariable String lockSerial, @PathVariable String gatewaySerial) {
        LockGatewayLinkId id = new LockGatewayLinkId(lockSerial, gatewaySerial);
        Optional<LockGatewayLink> link = linkRepository.findById(id);
        return link.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LockGatewayLink createLink(@Valid @RequestBody LockGatewayLink link) {
        if (link.getLockSerial() == null || link.getLockSerial().trim().isEmpty()) {
            throw new IllegalArgumentException("Lock serial cannot be empty");
        }
        if (link.getGatewaySerial() == null || link.getGatewaySerial().trim().isEmpty()) {
            throw new IllegalArgumentException("Gateway serial cannot be empty");
        }
        return linkRepository.save(link);
    }

    @PutMapping("/{lockSerial}/{gatewaySerial}")
    public ResponseEntity<LockGatewayLink> updateLink(@PathVariable String lockSerial, 
                                                     @PathVariable String gatewaySerial, 
                                                     @Valid @RequestBody LockGatewayLink linkDetails) {
        LockGatewayLinkId id = new LockGatewayLinkId(lockSerial, gatewaySerial);
        Optional<LockGatewayLink> optionalLink = linkRepository.findById(id);
        if (optionalLink.isPresent()) {
            LockGatewayLink link = optionalLink.get();
            link.setRssi(linkDetails.getRssi());
            return ResponseEntity.ok(linkRepository.save(link));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{lockSerial}/{gatewaySerial}")
    public ResponseEntity<?> deleteLink(@PathVariable String lockSerial, @PathVariable String gatewaySerial) {
        LockGatewayLinkId id = new LockGatewayLinkId(lockSerial, gatewaySerial);
        if (linkRepository.existsById(id)) {
            linkRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 