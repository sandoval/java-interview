package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.exception.ResourceNotFoundException;
import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.persistence.repository.LockRepository;
import com.vingcard.athos.interview.service.LockService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
	public Lock getLockBySerial(String serial) {
		return lockRepository.findById(serial)
				.orElseThrow(() -> new ResourceNotFoundException("Lock not found: " + serial));
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
	public Lock updateLock(String serial, Lock lockDetails) {
		Lock optionalLock = this.lockRepository.findById(serial)
				.orElseThrow(() -> new ResourceNotFoundException("Lock not found: " + serial));

		optionalLock.setName(lockDetails.getName());
		optionalLock.setMacAddress(lockDetails.getMacAddress());
		optionalLock.setVersion(lockDetails.getVersion());

		// Only update online status if explicitly provided
		if (lockDetails.isOnline() != optionalLock.isOnline()) {
			optionalLock.setOnline(lockDetails.isOnline());
		}

		return lockRepository.save(optionalLock);
	}


	/**
	 * Delete lock from Database filtered by serial
	 *
	 * @param serial Lock Serial ID
	 */
	@Override
	public void deleteLock(String serial) {
		Lock lock = lockRepository.findById(serial)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lock not found: " + serial));

		lockRepository.delete(lock);
	}


	/**
	 * Check if Lock existis by Serial ID
	 *
	 * @param serial Lock Serial ID
	 * @return boolean
	 */
	@Override
	public boolean existsById(String serial) {
		return lockRepository.existsById(serial);
	}
}
