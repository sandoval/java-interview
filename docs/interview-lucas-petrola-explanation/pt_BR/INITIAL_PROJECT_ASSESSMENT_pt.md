# Avaliação inicial do projeto

Este projeto é um desafio que simula a implementação do controle de acessos para trancas
e gateways num sistema de IoT.

Ele foi desenvolvido como uma API REST, que permite o resgate e edição das informações de trancas,
portas de entrada e links de acesso.

--- 

## Tecnologias

O desafio foi desenvolvido utilizando das tecnologias Java (minimamente na sua versão 21,
podendo ser também utilizada a versão 24), SpringBoot (Versão 3.4.6) e gerenciado pelo Gradle Kotlin DSL.

## Persistência e Base de dados

Foi utilizado o PostgreSQL, na sua versão mais recente, em um contâiner Docker,
via Docker Compose.

Também foi implementada uma camada de persistência utilizando JPA (Spring Data).
As classes de persistência se encontram nos pacotes a seguir:

* **Repositorios**: com.vingcard.athos.interview.persistence.repository
* **Entidades**: com.vingcard.athos.interview.persistence.entity

Também foi implementada uma classe auxiliar chamada ``DummyDataLoader``
que realiza a população de dados fictícios, a qual se encontra no pacote ``com.vingcard.athos.interview.persistence``.

> Esses dados de fíctícios são carregados no momento da inicialização do projeto caso o perfil ``dummydata`` esteja
> ativo

Segue abaixo o diagrama extraído da base de dados:

![Database Diagram](../assets/database_diagram.png)
<small>Extraído com Jetbrains DataGrip IDE.</small>

## Camada de controle

Foram implementados três classes de controle. Que seguem o nível 2 de maturidade da
implementação do Modelo REST. As três encontram no pacote ``com.vingcard.athos.interview.controller``.

Também foi adicionado um global ControllerAdvice dentro do mesmo pacote
de controllers.

# Configurações

O Desafio inicialmente implementa uma configuração básica do ``CORS``
e uma para os ``Resource Handlers``.

---

# Pontos fortes

* Facilidade de configuração do ambiente de desenvolvimento. Devido à implementação do PostgreSQL com o Docker Compose.
* Existe a implementação de testes automatizados em praticamente todas as camadas da aplicação.

# Pontos de melhoria

* Camada de serviço é inexistente.
* A Captura de exceções não implementa um padrão para respostas adequado. E se encontra dentro do mesmo pacote que
  controllers.
* A configuração do Postgres no Docker Compose não segue boas práticas.
* Classes DTO inexistentes.
* Em todos os ``@RequestMapping``, dento dos controllers repete-se o prefixo /api e não existe nenhuma identificação de
  versionamento.

