package com.vingcard.athos.interview.persistence;

import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
import com.vingcard.athos.interview.persistence.repository.LockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.Random;

@Component
@org.springframework.context.annotation.Profile("dummydata")
public class DummyDataLoader implements CommandLineRunner {
    private final GatewayRepository gatewayRepository;
    private final LockRepository lockRepository;
    private final Random random = new Random();

    @Autowired
    public DummyDataLoader(GatewayRepository gatewayRepository, LockRepository lockRepository) {
        this.gatewayRepository = gatewayRepository;
        this.lockRepository = lockRepository;
    }

    @Override
    public void run(String... args) {
        if (gatewayRepository.count() < 10) {
            for (int i = 1; i <= 10; i++) {
                String serial = String.format("ALKG%012d", i);
                String mac = randomMac();
                String version = "v" + (random.nextInt(5) + 1) + ".0.0";
                Gateway gateway = new Gateway(serial, mac, random.nextBoolean(), version);
                gatewayRepository.save(gateway);
            }
        }
        if (lockRepository.count() < 100) {
            for (int i = 1; i <= 100; i++) {
                String serial = String.format("ALKS%012d", i);
                String name = "Lock " + i;
                String mac = randomMac();
                String version = "v" + (random.nextInt(5) + 1) + ".0.0";
                Lock lock = new Lock(serial, name, mac, random.nextBoolean(), version);
                lockRepository.save(lock);
            }
        }
    }

    public String generateRandomMac() {
        byte[] macAddr = new byte[6];
        random.nextBytes(macAddr);
        StringBuilder sb = new StringBuilder(17);
        for (byte b : macAddr) {
            if (sb.length() > 0) sb.append(":");
            sb.append(String.format(Locale.US, "%02X", b));
        }
        return sb.toString();
    }

    private String randomMac() {
        return generateRandomMac();
    }
}
