package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.persistence.entity.Lock;

import java.util.List;

public interface LockService {

	List<Lock> getAllLocks();

	Lock getLockBySerial(String serial);

	Lock createLock(Lock lock);

	Lock updateLock(String serial, Lock lockDetails);

	void deleteLock(String serial);

	boolean existsById(String serial);
}
