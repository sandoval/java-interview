package com.vingcard.athos.interview;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;

import java.util.Optional;

@DataJpaTest
public class GatewayRepositoryTest {
    @Autowired
    private GatewayRepository gatewayRepository;

    @Test
    void testSaveAndFindGateway() {
        Gateway gateway = new Gateway("1234567890123456", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");
        gatewayRepository.save(gateway);

        Optional<Gateway> found = gatewayRepository.findById("1234567890123456");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("AA:BB:CC:DD:EE:FF", found.get().getMacAddress());
        Assertions.assertTrue(found.get().isOnline());
        Assertions.assertEquals("v1.0.0", found.get().getVersion());
    }

    @Test
    void testDefaultValues() {
        Gateway gateway = new Gateway();
        gateway.setSerial("0000000000000001");
        gateway.setMacAddress("00:11:22:33:44:55");
        gatewayRepository.save(gateway);

        Optional<Gateway> found = gatewayRepository.findById("0000000000000001");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertFalse(found.get().isOnline());
        Assertions.assertEquals("", found.get().getVersion());
    }
}
