package com.vingcard.athos.interview;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.persistence.repository.LockRepository;

import java.util.Optional;

@DataJpaTest
public class LockRepositoryTest {
    @Autowired
    private LockRepository lockRepository;

    @Test
    void testSaveAndFindLock() {
        Lock lock = new Lock("LOCKSERIAL000001", "Main Door", "11:22:33:44:55:66", true, "v2.1.0");
        lockRepository.save(lock);

        Optional<Lock> found = lockRepository.findById("LOCKSERIAL000001");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Main Door", found.get().getName());
        Assertions.assertEquals("11:22:33:44:55:66", found.get().getMacAddress());
        Assertions.assertTrue(found.get().isOnline());
        Assertions.assertEquals("v2.1.0", found.get().getVersion());
    }

    @Test
    void testDefaultValues() {
        Lock lock = new Lock();
        lock.setSerial("LOCKSERIAL000002");
        lock.setName("Back Door");
        lock.setMacAddress("22:33:44:55:66:77");
        lockRepository.save(lock);

        Optional<Lock> found = lockRepository.findById("LOCKSERIAL000002");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Back Door", found.get().getName());
        Assertions.assertEquals("22:33:44:55:66:77", found.get().getMacAddress());
        Assertions.assertFalse(found.get().isOnline());
        Assertions.assertEquals("", found.get().getVersion());
    }
}
