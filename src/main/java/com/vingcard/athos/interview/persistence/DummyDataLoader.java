package com.vingcard.athos.interview.persistence;

import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.persistence.entity.LockGatewayLink;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
import com.vingcard.athos.interview.persistence.repository.LockGatewayLinkRepository;
import com.vingcard.athos.interview.persistence.repository.LockRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
@org.springframework.context.annotation.Profile("dummydata")
@AllArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

	private final GatewayRepository gatewayRepository;
	private final LockRepository lockRepository;
	private final LockGatewayLinkRepository lockGatewayLinkRepository;
	private final Random random = new Random();


	/**
	 * Populate the local development Database with Dummy Data
	 *
	 * @param args Execution parameters
	 */
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

		if (lockGatewayLinkRepository.count() < gatewayRepository.count()) {
			LockGatewayLink lockGatewayLink = new LockGatewayLink();

			List<Lock> lockResult = lockRepository.findAll();
			List<Gateway> gatewayResult = gatewayRepository.findAll();

			for (int i = 0; i < gatewayResult.size(); i++) {
				if (i < gatewayResult.size() - 1) {
					lockGatewayLink.setGatewaySerial(gatewayResult.get(i).getSerial());
					lockGatewayLink.setLockSerial(lockResult.get(i).getSerial());
					lockGatewayLink.setRssi(randomRssi());
				} else {
					break;
				}

				lockGatewayLinkRepository.save(lockGatewayLink);
			}
		}
	}


	/**
	 * This method generate Random Mac Address for dummy Data Population
	 *
	 * @return String
	 */
	public String randomMac() {
		byte[] macAddr = new byte[6];
		random.nextBytes(macAddr);
		StringBuilder sb = new StringBuilder(17);

		for (byte b : macAddr) {
			if (!sb.isEmpty()) sb.append(":");
			sb.append(String.format(Locale.US, "%02X", b));
		}

		// Random Mac Address Generated
		return sb.toString();
	}


	/**
	 * This method generate Random RSSi Indicator for dummy Data Population
	 *
	 * @return double
	 */
	private static double randomRssi() {
		double minRssi = -100.0;
		double maxRssi = -30.0;
		return minRssi + new java.util.Random().nextDouble() * (maxRssi - minRssi);
	}
}
