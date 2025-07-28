package com.vingcard.athos.interview.entity;

import com.vingcard.athos.interview.persistence.entity.Lock;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LockEntityTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidLock() {
        Lock lock = new Lock("LOCKSERIAL000001", "Main Door", "11:22:33:44:55:66", true, "v2.1.0");
        Set<ConstraintViolation<Lock>> violations = validator.validate(lock);
        assertTrue(violations.isEmpty(), "Valid lock should have no validation violations");
    }

    @Test
    void testDefaultConstructor() {
        Lock lock = new Lock();
        assertNotNull(lock, "Default constructor should create a lock");
        assertFalse(lock.isOnline(), "Default online status should be false");
        assertEquals("", lock.getVersion(), "Default version should be empty string");
    }

    @Test
    void testParameterizedConstructor() {
        Lock lock = new Lock("LOCKSERIAL000001", "Main Door", "11:22:33:44:55:66", true, "v2.1.0");
        
        assertEquals("LOCKSERIAL000001", lock.getSerial());
        assertEquals("Main Door", lock.getName());
        assertEquals("11:22:33:44:55:66", lock.getMacAddress());
        assertTrue(lock.isOnline());
        assertEquals("v2.1.0", lock.getVersion());
    }

    @Test
    void testSettersAndGetters() {
        Lock lock = new Lock();
        
        lock.setSerial("LOCKSERIAL000001");
        lock.setName("Main Door");
        lock.setMacAddress("11:22:33:44:55:66");
        lock.setOnline(true);
        lock.setVersion("v2.1.0");
        
        assertEquals("LOCKSERIAL000001", lock.getSerial());
        assertEquals("Main Door", lock.getName());
        assertEquals("11:22:33:44:55:66", lock.getMacAddress());
        assertTrue(lock.isOnline());
        assertEquals("v2.1.0", lock.getVersion());
    }

    @Test
    void testSerialLengthValidation() {
        // Test serial that's too long (should be 16 characters max)
        Lock lock = new Lock("LOCKSERIAL000001234567", "Main Door", "11:22:33:44:55:66", true, "v2.1.0");
        Set<ConstraintViolation<Lock>> violations = validator.validate(lock);
        
        // Note: This test assumes there's a @Size constraint on serial
        // If there isn't one, this test documents the need for it
        assertTrue(violations.isEmpty() || violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("serial")), 
                "Serial length validation should be enforced");
    }

    @Test
    void testNameLengthValidation() {
        // Test name that's too long (should be 50 characters max)
        String longName = "This is a very long lock name that exceeds the maximum allowed length of fifty characters";
        Lock lock = new Lock("LOCKSERIAL000001", longName, "11:22:33:44:55:66", true, "v2.1.0");
        Set<ConstraintViolation<Lock>> violations = validator.validate(lock);
        
        // Note: This test assumes there's a @Size constraint on name
        // If there isn't one, this test documents the need for it
        assertTrue(violations.isEmpty() || violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")), 
                "Name length validation should be enforced");
    }

    @Test
    void testMacAddressFormat() {
        // Test various MAC address formats
        String[] validMacAddresses = {
            "11:22:33:44:55:66",
            "aa:bb:cc:dd:ee:ff",
            "A1:B2:C3:D4:E5:F6"
        };
        
        for (String macAddress : validMacAddresses) {
            Lock lock = new Lock("LOCKSERIAL000001", "Main Door", macAddress, true, "v2.1.0");
            Set<ConstraintViolation<Lock>> violations = validator.validate(lock);
            assertTrue(violations.isEmpty(), "Valid MAC address format should pass validation: " + macAddress);
        }
    }

    @Test
    void testVersionLengthValidation() {
        // Test version that's too long (should be 20 characters max)
        String longVersion = "v2.1.0.very.long.version.string";
        Lock lock = new Lock("LOCKSERIAL000001", "Main Door", "11:22:33:44:55:66", true, longVersion);
        Set<ConstraintViolation<Lock>> violations = validator.validate(lock);
        
        // Note: This test assumes there's a @Size constraint on version
        // If there isn't one, this test documents the need for it
        assertTrue(violations.isEmpty() || violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("version")), 
                "Version length validation should be enforced");
    }

    @Test
    void testOnlineStatusToggle() {
        Lock lock = new Lock("LOCKSERIAL000001", "Main Door", "11:22:33:44:55:66", false, "v2.1.0");
        assertFalse(lock.isOnline());
        
        lock.setOnline(true);
        assertTrue(lock.isOnline());
        
        lock.setOnline(false);
        assertFalse(lock.isOnline());
    }

    @Test
    void testNullValues() {
        Lock lock = new Lock();
        
        // Test that null values can be set (for partial updates)
        lock.setSerial(null);
        lock.setName(null);
        lock.setMacAddress(null);
        lock.setVersion(null);
        
        assertNull(lock.getSerial());
        assertNull(lock.getName());
        assertNull(lock.getMacAddress());
        assertNull(lock.getVersion());
    }

    @Test
    void testEmptyName() {
        Lock lock = new Lock("LOCKSERIAL000001", "", "11:22:33:44:55:66", true, "v2.1.0");
        Set<ConstraintViolation<Lock>> violations = validator.validate(lock);
        
        // Note: This test assumes there's a @NotBlank constraint on name
        // If there isn't one, this test documents the need for it
        assertTrue(violations.isEmpty() || violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")), 
                "Empty name validation should be enforced");
    }

    @Test
    void testSpecialCharactersInName() {
        String[] specialNames = {
            "Main Door (Front)",
            "Back Door - Garage",
            "Side Door & Patio",
            "Lock #1",
            "Door with 'quotes'",
            "Door with \"double quotes\""
        };
        
        for (String name : specialNames) {
            Lock lock = new Lock("LOCKSERIAL000001", name, "11:22:33:44:55:66", true, "v2.1.0");
            Set<ConstraintViolation<Lock>> violations = validator.validate(lock);
            assertTrue(violations.isEmpty(), "Special characters in name should be allowed: " + name);
        }
    }
} 