package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.exception.NotFoundExceptionResponse;
import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.persistence.repository.LockRepository;
import com.vingcard.athos.interview.service.LockService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LockServiceImpl implements LockService {

	private final LockRepository lockRepository;


	/**
	 * Get all registered locks from database
	 *
	 * @return List with all locks
	 */
	@Override
	public List<Lock> getAllLocks() {
		return lockRepository.findAll();
	}


	/**
	 * Locks filtered by serial
	 *
	 * @param serial Lock Serial ID
	 * @return Response status and Lock Object
	 */
	@Override
	public ResponseEntity<Lock> getLockBySerial(String serial) {
		Optional<Lock> lock = lockRepository.findById(serial);

		if (lock.isEmpty()) {
			throw new NotFoundExceptionResponse("Lock not found with serial: " + serial);
		}

		return lock.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}


	/**
	 * Add new lock to database
	 *
	 * @param lock Lock Object
	 * @return Response status with new lock object created
	 */
	@Override
	public Lock createLock(Lock lock) {
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


	/**
	 * Update lock filtered by serial
	 *
	 * @param serial      Lock Serial ID
	 * @param lockDetails Lock object
	 * @return Update status response
	 */
	@Override
	public ResponseEntity<Lock> updateLock(String serial, Lock lockDetails) {
		Optional<Lock> optionalLock = this.lockRepository.findById(serial);

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
			throw new NotFoundExceptionResponse("Lock not found with serial: " + serial);
		}
	}


	/**
	 * Delete lock from Database filtered by serial
	 *
	 * @param serial Lock Serial ID
	 * @return Deletion status response
	 */
	@Override
	public ResponseEntity<?> deleteLock(String serial) {
		if (lockRepository.existsById(serial)) {
			lockRepository.deleteById(serial);
			return ResponseEntity.ok().build();
		} else {
			throw new NotFoundExceptionResponse("Lock not found with serial: " + serial);
		}
	}
}
