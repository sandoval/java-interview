# Relatório de ‘bugs’

Foram encontrados alguns ‘bugs’ no meio do processo de refactor da aplicação.

---

## 1. Tabela ``lock_gateway_link`` não está a ser populada

A classe ``DummyDataLoader.java`` que é responsável por popular as tabelas com dados fictícios não implementou nenhum
trecho que insere os dados na tabela ``lock_gateway_link``.

### Resolução

Implementar o seguinte trecho:

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

E declarar um método para gerar RSSIs randômicos:

```java
private static double randomRssi() {
	double minRssi = -100.0;
	double maxRssi = -30.0;
	return minRssi + new java.util.Random().nextDouble() * (maxRssi - minRssi);
}
```

---

2. Endpoint ``http://localhost:8080/api/lock-gateway-links`` retorna o seguinte erro. 

```json
{
    "timestamp": "2025-07-31T19:50:30.4934778",
    "success": false,
    "code": 500,
    "message": "Type definition error: [simple type, class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor]"
}
```

### Resolução

A classe Jackson não conseguia serializar o retorno da associação das tabelas com ``@ManyToOne(fetch = FetchType.LAZY)``
A solução foi atribuir essas associações com 

```java
@ManyToOne(fetch = FetchType.EAGER)
```

--- 

# Referências

* [Stack overflow - #BugReport 2](https://stackoverflow.com/questions/52656517/no-serializer-found-for-class-org-hibernate-proxy-pojo-bytebuddy-bytebuddyinterc)
