package com.vingcard.athos.interview.repository;

import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.model.dto.LockGatewayLinkIdDto;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
import com.vingcard.athos.interview.persistence.repository.LockGatewayLinkRepository;
import com.vingcard.athos.interview.persistence.repository.LockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LockGatewayLinkRepositoryTest {
    @Autowired
    private LockRepository lockRepository;
    @Autowired
    private GatewayRepository gatewayRepository;
    @Autowired
    private LockGatewayLinkRepository linkRepository;

	private LockGatewayLink testLink;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        linkRepository.deleteAll();
        lockRepository.deleteAll();
        gatewayRepository.deleteAll();

        // Create test data
	    Lock testLock = new Lock("LOCK000000000001", "Test Lock", "11:22:33:44:55:66", true, "v1.0.0");
	    Gateway testGateway = new Gateway("GATEWAY00000001", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");
        
        lockRepository.save(testLock);
        gatewayRepository.save(testGateway);
        
        testLink = new LockGatewayLink("LOCK000000000001", "GATEWAY00000001", -55.5);
    }

    @Test
    void testSaveAndFindLink() {
        linkRepository.save(testLink);

        Optional<LockGatewayLink> found = linkRepository.findById(new LockGatewayLinkIdDto("LOCK000000000001", "GATEWAY00000001"));
        assertTrue(found.isPresent());
        assertEquals(-55.5, found.get().getRssi());
        assertEquals("LOCK000000000001", found.get().getLockSerial());
        assertEquals("GATEWAY00000001", found.get().getGatewaySerial());
    }

    @Test
    void testFindByLockSerial() {
        // Create multiple links for the same lock
        Lock lock2 = new Lock("LOCK000000000002", "Test Lock 2", "22:33:44:55:66:77", true, "v1.0.0");
        Gateway gateway2 = new Gateway("GATEWAY00000002", "BB:CC:DD:EE:FF:00", true, "v1.0.0");
        
        lockRepository.save(lock2);
        gatewayRepository.save(gateway2);

        LockGatewayLink link1 = new LockGatewayLink("LOCK000000000001", "GATEWAY00000001", -55.5);
        LockGatewayLink link2 = new LockGatewayLink("LOCK000000000001", "GATEWAY00000002", -60.2);
        LockGatewayLink link3 = new LockGatewayLink("LOCK000000000002", "GATEWAY00000001", -45.8);

        linkRepository.save(link1);
        linkRepository.save(link2);
        linkRepository.save(link3);

        List<LockGatewayLink> linksForLock1 = linkRepository.findByLockSerial("LOCK000000000001");
        assertEquals(2, linksForLock1.size());
        
        // Verify both links belong to the same lock
        linksForLock1.forEach(link -> assertEquals("LOCK000000000001", link.getLockSerial()));
        
        // Verify different gateways
        List<String> gatewaySerials = linksForLock1.stream()
                .map(LockGatewayLink::getGatewaySerial)
                .toList();
        assertTrue(gatewaySerials.contains("GATEWAY00000001"));
        assertTrue(gatewaySerials.contains("GATEWAY00000002"));
    }

    @Test
    void testFindByGatewaySerial() {
        // Create multiple links for the same gateway
        Lock lock2 = new Lock("LOCK000000000002", "Test Lock 2", "22:33:44:55:66:77", true, "v1.0.0");
        Gateway gateway2 = new Gateway("GATEWAY00000002", "BB:CC:DD:EE:FF:00", true, "v1.0.0");
        
        lockRepository.save(lock2);
        gatewayRepository.save(gateway2);

        LockGatewayLink link1 = new LockGatewayLink("LOCK000000000001", "GATEWAY00000001", -55.5);
        LockGatewayLink link2 = new LockGatewayLink("LOCK000000000002", "GATEWAY00000001", -60.2);
        LockGatewayLink link3 = new LockGatewayLink("LOCK000000000001", "GATEWAY00000002", -45.8);

        linkRepository.save(link1);
        linkRepository.save(link2);
        linkRepository.save(link3);

        List<LockGatewayLink> linksForGateway1 = linkRepository.findByGatewaySerial("GATEWAY00000001");
        assertEquals(2, linksForGateway1.size());
        
        // Verify both links belong to the same gateway
        linksForGateway1.forEach(link -> assertEquals("GATEWAY00000001", link.getGatewaySerial()));
        
        // Verify different locks
        List<String> lockSerials = linksForGateway1.stream()
                .map(LockGatewayLink::getLockSerial)
                .toList();
        assertTrue(lockSerials.contains("LOCK000000000001"));
        assertTrue(lockSerials.contains("LOCK000000000002"));
    }

    @Test
    void testFindByLockSerialWithNoResults() {
        List<LockGatewayLink> links = linkRepository.findByLockSerial("NONEXISTENT");
        assertTrue(links.isEmpty());
    }

    @Test
    void testFindByGatewaySerialWithNoResults() {
        List<LockGatewayLink> links = linkRepository.findByGatewaySerial("NONEXISTENT");
        assertTrue(links.isEmpty());
    }

    @Test
    void testUpdateRssi() {
        linkRepository.save(testLink);

        // Update RSSI
        LockGatewayLink updatedLink = new LockGatewayLink("LOCK000000000001", "GATEWAY00000001", -45.0);
        linkRepository.save(updatedLink);

        Optional<LockGatewayLink> found = linkRepository.findById(new LockGatewayLinkIdDto("LOCK000000000001", "GATEWAY00000001"));
        assertTrue(found.isPresent());
        assertEquals(-45.0, found.get().getRssi());
    }

    @Test
    void testDeleteLink() {
        linkRepository.save(testLink);

        // Verify link exists
        assertTrue(linkRepository.existsById(new LockGatewayLinkIdDto("LOCK000000000001", "GATEWAY00000001")));

        // Delete link
        linkRepository.deleteById(new LockGatewayLinkIdDto("LOCK000000000001", "GATEWAY00000001"));

        // Verify link is deleted
        assertFalse(linkRepository.existsById(new LockGatewayLinkIdDto("LOCK000000000001", "GATEWAY00000001")));
    }

    @Test
    void testFindAllLinks() {
        Lock lock2 = new Lock("LOCK000000000002", "Test Lock 2", "22:33:44:55:66:77", true, "v1.0.0");
        Gateway gateway2 = new Gateway("GATEWAY00000002", "BB:CC:DD:EE:FF:00", true, "v1.0.0");
        
        lockRepository.save(lock2);
        gatewayRepository.save(gateway2);

        LockGatewayLink link1 = new LockGatewayLink("LOCK000000000001", "GATEWAY00000001", -55.5);
        LockGatewayLink link2 = new LockGatewayLink("LOCK000000000002", "GATEWAY00000002", -60.2);

        linkRepository.save(link1);
        linkRepository.save(link2);

        List<LockGatewayLink> allLinks = linkRepository.findAll();
        assertEquals(2, allLinks.size());
    }

    @Test
    void testComplexRssiValues() {
        // Test various RSSI values including edge cases
        double[] rssiValues = {-100.0, -50.5, 0.0, 50.5, 100.0};

	    for (double rssiValue : rssiValues) {
		    LockGatewayLink link = new LockGatewayLink("LOCK000000000001", "GATEWAY00000001", rssiValue);
		    linkRepository.save(link);

		    Optional<LockGatewayLink> found = linkRepository.findById(new LockGatewayLinkIdDto("LOCK000000000001", "GATEWAY00000001"));
		    assertTrue(found.isPresent());
		    assertEquals(rssiValue, found.get().getRssi(), 0.001);

		    // Clean up for next iteration
		    linkRepository.delete(link);
	    }
    }

    @Test
    void testLinkWithDifferentRssiValues() {
        // Create multiple links with different RSSI values
        Lock lock2 = new Lock("LOCK000000000002", "Test Lock 2", "22:33:44:55:66:77", true, "v1.0.0");
        lockRepository.save(lock2);

        LockGatewayLink link1 = new LockGatewayLink("LOCK000000000001", "GATEWAY00000001", -55.5);
        LockGatewayLink link2 = new LockGatewayLink("LOCK000000000002", "GATEWAY00000001", -60.2);

        linkRepository.save(link1);
        linkRepository.save(link2);

        List<LockGatewayLink> linksForGateway = linkRepository.findByGatewaySerial("GATEWAY00000001");
        assertEquals(2, linksForGateway.size());
        
        // Verify different RSSI values are preserved
        List<Double> rssiValues = linksForGateway.stream()
                .map(LockGatewayLink::getRssi)
                .toList();
        assertTrue(rssiValues.contains(-55.5));
        assertTrue(rssiValues.contains(-60.2));
    }
} 