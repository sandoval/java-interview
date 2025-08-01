# Bug Report

Some bugs were found during the application's refactor process.

---

## 1. `lock_gateway_link` table is not being populated

The `DummyDataLoader.java` class, responsible for populating the tables with dummy data, did not implement any code to
insert data into the `lock_gateway_link` table.

### Resolution

Implement the following code snippet:

```java
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
```

Also, declare a method to generate random RSSI values:

```java
private static double randomRssi() {
	double minRssi = -100.0;
	double maxRssi = -30.0;
	return minRssi + new java.util.Random().nextDouble() * (maxRssi - minRssi);
}
```

---

## 2. Endpoint `http://localhost:8080/api/lock-gateway-links` returns the following error:

```json
{
  "timestamp": "2025-07-31T19:50:30.4934778",
  "success": false,
  "code": 500,
  "message": "Type definition error: [simple type, class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor]"
}
```

### Resolution

Jackson was unable to serialize the association between entities due to the use of
`@ManyToOne(fetch = FetchType.LAZY)`.  
The solution was to change those associations to:

```java
@ManyToOne(fetch = FetchType.EAGER)
```

---

# References

* [Stack Overflow – #BugReport 2](https://stackoverflow.com/questions/52656517/no-serializer-found-for-class-org-hibernate-proxy-pojo-bytebuddy-bytebuddyinterc)
