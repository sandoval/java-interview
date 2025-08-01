package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.service.impl.LockServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locks")
@AllArgsConstructor
public class LockController {

	private final LockServiceImpl lockService;

	@GetMapping
	public List<Lock> getAllLocks() {
		return lockService.getAllLocks();
	}

	@GetMapping("/{serial}")
	public ResponseEntity<Lock> getLockBySerial(@PathVariable String serial) {
		return lockService.getLockBySerial(serial);
	}

	@PostMapping
	public Lock createLock(@Valid @RequestBody Lock lock) {
		return lockService.createLock(lock);
	}

	@PutMapping("/{serial}")
	public ResponseEntity<Lock> updateLock(@PathVariable String serial, @Valid @RequestBody Lock lockDetails) {
		return lockService.updateLock(serial, lockDetails);
	}

	@DeleteMapping("/{serial}")
	public ResponseEntity<?> deleteLock(@PathVariable String serial) {
		return lockService.deleteLock(serial);
	}
} 