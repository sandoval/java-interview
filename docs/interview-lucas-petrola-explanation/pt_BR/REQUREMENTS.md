# Documento de Requisitos ASSA ABLOY CHALLENGE

## 1. Introdução

# 1.1 Propósito do Documento de Requisitos

Esse documento especifica as características e funcionalidades do projeto que é um desafio que simula a implementação do
controle de acessos para trancas
e gateways num sistema de IoT.

## 1.2 Escopo do Produto

Ele foi desenvolvido como uma API REST, que permite o resgate e edição das informações de trancas,
portas de entrada e links de acesso. Deverá ser capaz de acessar, resgatar e editar as
informações das trancas de acessos do usuário.

## 1.3 Visão Geral do Restante do Documento

* A Seção 2 contém uma descrição das perspectivas do produto, de suas funcionalidades, das
  restrições gerais, de suposições e dependências.

* A Seção 3 apresentada os requisitos funcionais, não funcionais e de interface interna.

# 2. Descrição Geral

## 2.1 Perspectiva do Produto

O sistema visa facilitar o monitoramento, edição, alteração e resgate de informações referentes aos status das trancas,
portões e
links, conectados à um sistema de IoT.

> O sistema simulará o acesso com periféricos externos.

## 2.2 Funções do Produto

O sistema deve permitir o monitoramento, inserção e atualização de dados dessas trancas, portões e links. Deve existir o
controle de acesso de acordo com o perfil de cada usuário usuário.

## 2.3 Características do Usuário

Os utilizadores do sistema serão os consumidores finais, que devem ter familiaridade com tecnologia para a
utilização do sistema.

## 2.4 Restrições Gerais

O acesso a todos os terminais devem ser autenticados por um usuário previamente
registrado. Haverá dois perfis de acesso possíveis: ``Leitura`` e ``Gravação``. Os terminais que
fazem alterações no banco de dados estarão disponíveis apenas para usuários com perfil de
``acesso de gravação``. Os terminais que apenas recuperam e exibem informações estarão
disponíveis apenas para usuários com perfil de ``acesso de leitura``.

## 2.5 Suposições e Dependências

O sistema foi projetado para utilizar do banco de dados PostgreSQL, na sua versão mais recente,
em um contâiner Docker, via Docker Compose e implementada uma camada de persistência
utilizando JPA (Spring Data).

A autenticação foi desenvolvida usando o OAuth2 com AWS Cognito.

Para a autorização foi implementado o JWT para geração de tokens de acesso com
liberação de dados no próprio token e datas de validade para expiração dos tokens.

# 3. Requisitos Específicos

## 3.1.1 Funções Básicas de autenticação

* **RF_B1**: Deve haver um ponto central de autenticação para todos os usuários e aplicativos,
  facilitando adicionar novos aplicativos para o mesmo grupo de usuários.
* **RF_B2**: A solução de autenticação deve suportar endereços de e-mail para identificação do
  usuário.
* **RF_B3**: A solução de autenticação deve suportar a criação de contas self-service pelo
  usuário, exigindo apenas a verificação do e-mail.
* **RF_B4**: As contas de usuário devem ter o perfil de acesso de leitura atribuído
  automaticamente no momento da criação da conta.
* **RF_B5**: A solução de autenticação deve suportar centenas de milhares de usuários.
* **RF_B6**: A interface de usuário de autenticação deve ser localizável e disponível pelo menos
  em inglês, português e espanhol.
* **RF_B7**: A solução de autenticação deve suportar números de telefone para identificação do
  usuário no futuro.
* **RF_B8**: A solução de autenticação deve suportar integração SSO no futuro.
* **RF_B9**: A autenticação deve ser feita num navegador atualizado fornecido pelo sistema
  operacional.

## 3.1.2 Funções Fundamentais

* **RF_F1**: O sistema deve permitir a consulta aos gateway instalados, e exibir os seus status;
* **RF_F2**: O sistema deve permitir a consulta às trancas instaladas, e exibir os seus status;
* **RF_F3**: O sistema deve permitir a consulta aos links de Trancas e Gateways instalados, e exibir a integração e os
  seus status;

## 3.2 Requisitos de Qualidade

### 3.2.1 Funcionalidade

* **RF_QF 1.1**: O sistema contém funções para facilitar o monitoramento dos sistemas de trancas, portões e as suas
  integrações.
* **RF_QF 1.2**: Para ter acesso ao sistema o usuário é identificado de forma única, permitindo
  assim, que o sistema proteja as informações e forneça apenas às pessoas autorizadas.

### 3.2.2 Usabilidade

* **RF_QU 1.1**: O Sitema deve ser de fácil manuseio, podendo ser aprendido por qualquer usuário familiarizado com
  tecnologia.

### 3.2.3 Eficiência

* **RF_QE 1.1**: O tempo de resposta às consultas deve ser rápido, não podendo ultrapassar de 3 segundos de latência.

## 3.3 Requisitos de Interface Externa

### 3.3.1 Interfaces com o Usuário

> Todo fluxo de autenticação será realizado através do backend, um navegador e de soluções HTTP.