package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.persistence.repository.LockRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locks")
@CrossOrigin(origins = "http://localhost:3000")
public class LockController {

    private final LockRepository lockRepository;

    @Autowired
    public LockController(LockRepository lockRepository) {
        this.lockRepository = lockRepository;
    }

    @GetMapping
    public List<Lock> getAllLocks() {
        return lockRepository.findAll();
    }

    @GetMapping("/{serial}")
    public ResponseEntity<Lock> getLockBySerial(@PathVariable String serial) {
        Optional<Lock> lock = lockRepository.findById(serial);
        return lock.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Lock createLock(@Valid @RequestBody Lock lock) {
        if (lock.getSerial() == null || lock.getSerial().trim().isEmpty()) {
            throw new IllegalArgumentException("Serial cannot be empty");
        }
        // Set default values for new locks
        if (lock.getVersion() == null) {
            lock.setVersion("");
        }
        lock.setOnline(false); // New locks start offline
        return lockRepository.save(lock);
    }

    @PutMapping("/{serial}")
    public ResponseEntity<Lock> updateLock(@PathVariable String serial, @Valid @RequestBody Lock lockDetails) {
        Optional<Lock> optionalLock = lockRepository.findById(serial);
        if (optionalLock.isPresent()) {
            Lock lock = optionalLock.get();
            lock.setName(lockDetails.getName());
            lock.setMacAddress(lockDetails.getMacAddress());
            lock.setVersion(lockDetails.getVersion());
            // Only update online status if explicitly provided
            if (lockDetails.isOnline() != lock.isOnline()) {
                lock.setOnline(lockDetails.isOnline());
            }
            return ResponseEntity.ok(lockRepository.save(lock));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{serial}")
    public ResponseEntity<?> deleteLock(@PathVariable String serial) {
        if (lockRepository.existsById(serial)) {
            lockRepository.deleteById(serial);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 