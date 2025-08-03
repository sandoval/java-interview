package com.vingcard.athos.interview;

import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.model.dto.LockGatewayLinkIdDto;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
import com.vingcard.athos.interview.persistence.repository.LockGatewayLinkRepository;
import com.vingcard.athos.interview.persistence.repository.LockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class LockGatewayLinkRepositoryTest {
    @Autowired
    private LockRepository lockRepository;
    @Autowired
    private GatewayRepository gatewayRepository;
    @Autowired
    private LockGatewayLinkRepository linkRepository;

    @Test
    void testSaveAndFindLink() {
        Lock lock = new Lock("LOCKSERIAL000003", "Side Door", "33:44:55:66:77:88", false, "");
        lockRepository.save(lock);
        Gateway gateway = new Gateway("GATEWAYSERIAL0001", "AA:BB:CC:DD:EE:FF", false, "");
        gatewayRepository.save(gateway);

        LockGatewayLink link = new LockGatewayLink("LOCKSERIAL000003", "GATEWAYSERIAL0001", -55.5);
        linkRepository.save(link);

        Optional<LockGatewayLink> found = linkRepository.findById(new LockGatewayLinkIdDto("LOCKSERIAL000003", "GATEWAYSERIAL0001"));
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(-55.5, found.get().getRssi());
        Assertions.assertEquals("LOCKSERIAL000003", found.get().getLockSerial());
        Assertions.assertEquals("GATEWAYSERIAL0001", found.get().getGatewaySerial());
    }
}
