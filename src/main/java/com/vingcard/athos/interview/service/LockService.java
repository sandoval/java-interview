package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.persistence.entity.Lock;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LockService {

	List<Lock> getAllLocks();

	ResponseEntity<Lock> getLockBySerial(String serial);

	Lock createLock(Lock lock);

	ResponseEntity<Lock> updateLock(String serial, Lock lockDetails);

	ResponseEntity<?> deleteLock(String serial);
}
