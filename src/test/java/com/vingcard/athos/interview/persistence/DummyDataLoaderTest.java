package com.vingcard.athos.interview.persistence;

import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
import com.vingcard.athos.interview.persistence.repository.LockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dummydata")
class DummyDataLoaderTest {

    @Autowired
    private DummyDataLoader dummyDataLoader;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private LockRepository lockRepository;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        gatewayRepository.deleteAll();
        lockRepository.deleteAll();
    }

    @Test
    void testDataLoaderCreatesGateways() {
        // Run the data loader
        dummyDataLoader.run();

        // Verify gateways were created
        List<Gateway> gateways = gatewayRepository.findAll();
        assertEquals(10, gateways.size(), "Should create exactly 10 gateways");

        // Verify gateway properties
        for (int i = 0; i < gateways.size(); i++) {
            Gateway gateway = gateways.get(i);
            String expectedSerial = String.format("ALKG%012d", i + 1);
            assertEquals(expectedSerial, gateway.getSerial(), "Gateway serial should match expected format");
            assertNotNull(gateway.getMacAddress(), "MAC address should not be null");
            assertTrue(isValidMacAddress(gateway.getMacAddress()), "MAC address should be valid format");
            assertNotNull(gateway.getVersion(), "Version should not be null");
            assertTrue(gateway.getVersion().startsWith("v"), "Version should start with 'v'");
        }
    }

    @Test
    void testDataLoaderCreatesLocks() {
        // Run the data loader
        dummyDataLoader.run();

        // Verify locks were created
        List<Lock> locks = lockRepository.findAll();
        assertEquals(100, locks.size(), "Should create exactly 100 locks");

        // Verify lock properties
        for (int i = 0; i < locks.size(); i++) {
            Lock lock = locks.get(i);
            String expectedSerial = String.format("ALKS%012d", i + 1);
            assertEquals(expectedSerial, lock.getSerial(), "Lock serial should match expected format");
            assertEquals("Lock " + (i + 1), lock.getName(), "Lock name should match expected format");
            assertNotNull(lock.getMacAddress(), "MAC address should not be null");
            assertTrue(isValidMacAddress(lock.getMacAddress()), "MAC address should be valid format");
            assertNotNull(lock.getVersion(), "Version should not be null");
            assertTrue(lock.getVersion().startsWith("v"), "Version should start with 'v'");
        }
    }

    @Test
    void testDataLoaderDoesNotDuplicateData() {
        // Run the data loader twice
        dummyDataLoader.run();
        dummyDataLoader.run();

        // Verify no duplicates were created
        assertEquals(10, gatewayRepository.count(), "Should still have exactly 10 gateways");
        assertEquals(100, lockRepository.count(), "Should still have exactly 100 locks");
    }

    @Test
    void testDataLoaderWithExistingData() {
        // Create some existing data
        Gateway existingGateway = new Gateway("ALKG000000000001", "AA:BB:CC:DD:EE:FF", true, "v1.0.0");
        Lock existingLock = new Lock("ALKS000000000001", "Existing Lock", "11:22:33:44:55:66", true, "v1.0.0");
        
        gatewayRepository.save(existingGateway);
        lockRepository.save(existingLock);

        // Run the data loader
        dummyDataLoader.run();

        // Verify existing data is preserved and new data is added
        assertEquals(10, gatewayRepository.count(), "Should have exactly 10 gateways total");
        assertEquals(100, lockRepository.count(), "Should have exactly 100 locks total");

        // Verify existing data still exists
        assertTrue(gatewayRepository.findById("ALKG000000000001").isPresent(), "Existing gateway should be preserved");
        assertTrue(lockRepository.findById("ALKS000000000001").isPresent(), "Existing lock should be preserved");
    }

    @Test
    void testMacAddressGeneration() {
        // Test the MAC address generation method directly
        String mac1 = dummyDataLoader.randomMac();
        String mac2 = dummyDataLoader.randomMac();

        // Verify MAC address format
        assertTrue(isValidMacAddress(mac1), "Generated MAC address should be valid");
        assertTrue(isValidMacAddress(mac2), "Generated MAC address should be valid");

        // Verify MAC addresses are different (random)
        assertNotEquals(mac1, mac2, "Generated MAC addresses should be different");
    }

    @Test
    void testVersionGeneration() {
        // Run the data loader
        dummyDataLoader.run();

        // Check that versions are in expected range (v1.0.0 to v5.0.0)
        List<Gateway> gateways = gatewayRepository.findAll();
        for (Gateway gateway : gateways) {
            assertTrue(gateway.getVersion().matches("v[1-5]\\.0\\.0"), 
                    "Gateway version should be in range v1.0.0 to v5.0.0: " + gateway.getVersion());
        }

        List<Lock> locks = lockRepository.findAll();
        for (Lock lock : locks) {
            assertTrue(lock.getVersion().matches("v[1-5]\\.0\\.0"), 
                    "Lock version should be in range v1.0.0 to v5.0.0: " + lock.getVersion());
        }
    }

    @Test
    void testSerialNumberFormat() {
        // Run the data loader
        dummyDataLoader.run();

        // Verify gateway serial format
        List<Gateway> gateways = gatewayRepository.findAll();
        for (int i = 0; i < gateways.size(); i++) {
            String serial = gateways.get(i).getSerial();
            assertTrue(serial.matches("ALKG\\d{12}"), "Gateway serial should match ALKG + 12 digits: " + serial);
            assertEquals(String.format("ALKG%012d", i + 1), serial, "Gateway serial should be sequential");
        }

        // Verify lock serial format
        List<Lock> locks = lockRepository.findAll();
        for (int i = 0; i < locks.size(); i++) {
            String serial = locks.get(i).getSerial();
            assertTrue(serial.matches("ALKS\\d{12}"), "Lock serial should match ALKS + 12 digits: " + serial);
            assertEquals(String.format("ALKS%012d", i + 1), serial, "Lock serial should be sequential");
        }
    }

    @Test
    void testOnlineStatusDistribution() {
        // Run the data loader
        dummyDataLoader.run();

        // Check that both online and offline devices exist (random distribution)
        List<Gateway> gateways = gatewayRepository.findAll();
        long onlineGateways = gateways.stream().filter(Gateway::isOnline).count();
        long offlineGateways = gateways.stream().filter(g -> !g.isOnline()).count();

        assertTrue(onlineGateways > 0, "Should have some online gateways");
        assertTrue(offlineGateways > 0, "Should have some offline gateways");

        List<Lock> locks = lockRepository.findAll();
        long onlineLocks = locks.stream().filter(Lock::isOnline).count();
        long offlineLocks = locks.stream().filter(l -> !l.isOnline()).count();

        assertTrue(onlineLocks > 0, "Should have some online locks");
        assertTrue(offlineLocks > 0, "Should have some offline locks");
    }

    private boolean isValidMacAddress(String macAddress) {
        // MAC address format: XX:XX:XX:XX:XX:XX where X is hex digit
        Pattern macPattern = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
        return macPattern.matcher(macAddress).matches();
    }
} 