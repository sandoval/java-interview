package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.service.LockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locks")
@AllArgsConstructor
public class LockController {

	private final LockService lockService;


	/**
	 * Get All locks from database
	 * Path: /api/locks/
	 * Method: GET
	 *
	 * @return List of all Locks from database
	 */
	@GetMapping
	public List<Lock> getAllLocks() {
		return lockService.getAllLocks();
	}


	/**
	 * Returns Lock from database filtered by Lock Serial ID
	 * Path: /api/locks/{serial}
	 * Method: GET
	 *
	 * @param serial Lock Serial ID
	 * @return Existing lock Object
	 */
	@GetMapping("/{serial}")
	public ResponseEntity<Lock> getLockBySerial(@PathVariable String serial) {
		Lock lock = lockService.getLockBySerial(serial);
		if (lock == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lock);
	}


	/**
	 * Add a new lock to Database
	 * Path: /api/locks
	 * Method: POST
	 *
	 * @param lock Lock Object
	 * @return Lock newly created
	 */
	@PostMapping
	public Lock createLock(@Valid @RequestBody Lock lock) {
		return lockService.createLock(lock);
	}


	/**
	 * Update lock filtered by Lock Serial ID from database
	 * Path: /api/locks/{serial}
	 * Method: PUT
	 *
	 * @param serial      Lock Serial ID
	 * @param lockDetails Lock Object
	 * @return Lock newly updated
	 */
	@PutMapping("/{serial}")
	public Lock updateLock(@PathVariable String serial, @Valid @RequestBody Lock lockDetails) {
		return lockService.updateLock(serial, lockDetails);
	}


	/**
	 * Delete lock filtered by Lock Serial ID from database
	 * Path: /api/locks/{serial}
	 * Method: DELETE
	 *
	 * @param serial Lock Serial ID
	 * @return Return Status code 202 if success
	 */
	@DeleteMapping("/{serial}")
	public ResponseEntity<?> deleteLock(@PathVariable String serial) {
		if (!lockService.existsById(serial)) {
			return ResponseEntity.notFound().build();
		}

		lockService.deleteLock(serial);
		return ResponseEntity.ok().build();
	}
} 