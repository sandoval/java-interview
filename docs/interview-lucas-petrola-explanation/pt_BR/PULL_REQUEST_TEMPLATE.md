# Descrição

## 1. Problema

### 1.1 Ausência de autenticação no projeto original

O projeto não possuía qualquer mecanismo de autenticação ou autorização implementado.
Todas as rotas estavam públicas, o que representa alto risco para qualquer aplicação que manipule dados sensíveis, ou
que permita o controle de acesso a funcionalidades que possam ser nocivas à segurança do utilizador final, caso caia em
mãos erradas.

### 1.1.2 Consequências da ausência de autenticação:

* Qualquer pessoa poderia consumir os endpoints da API, independentemente de ser um utilizador legítimo;
* Não era possível gerir permissões de acesso, já que não havia identificação de utilizadores;
* Não havia mecanismos de expiração e/ou revogação de acesso aos utilizadores Possibilitando controle total da aplicação
  por períodos indefinidos;
* Qualquer dado exposto pela API estava vulnerável a uso indevido de robôs e outros meios de automação;

### 1.2 Arquitetura do projeto

O projeto não possuía separação de responsabilidades em algumas camadas da aplicação.

* Havia a ausência do modelo DTO, onde era exibido ao usuário exatamente o que se encontrava nas entidades;
* A responsabilidade da camada de serviços estava sendo implementada dentro das classes de controle;
* Não foi configurada nenhuma internacionalização. Permitindo assim, a apresentação de apenas um idioma;
* Alguns testes não estavam sendo aprovados;
* Configuração incompleta da base de dados via Docker-compose;
* Não havia documentação do sistema;

## 2. Solução

### 2.1 Implementação de autenticação com AWS Cognito + JWT

Como forma de resolução deste problema, foi implementado um fluxo de ``OAuth2 Authorization Code`` com ``AWS Cognito``
como provedor de identidade. A autenticação passou a ser baseada em ``tokens JWT``, que permitem:

* Verificação segura da identidade do utilizador em cada requisição via ``access_token``;
* Extração de dados do utilizador autenticado a partir do ‘token’;
* Controle de expiração automática de sessões;
* Mapeamento de perfis de acesso com base em grupos do ``AWS Cognito``;

### 2.1.2 Opção pelo AWS Cognito

O AWS Cognito é um serviço oferecido pela Amazon Web Services (AWS) que fornece todo o fluxo de autenticação,
autorização e gestão das contas e grupos de usuários. Ele permite a implementação de ‘login’ seguro sem a necessidade de
construção de toda uma infraestrutura do zero.

Ele suporta diversos padrões de segurança como OAuth2, OpenID Connect e SAML 2.0. Os ‘tokens’ emitidos podem ser
utilizados em APIs protegidas com JWT.

A documentação é robusta e existe suporte amplo da comunidade. Sendo uma solução estável, confiável e escalável. Sendo
uma das soluções mais bem quistas do mercado. Oferece uma gama de opções de recursos e excelente aceitação de mercado.

### 2.1.2.1 Ele fornece Pools de usuários com autenticação pronta, as quais incluem:

* Login com Email, telefone e Senha;
* Ferramentas de verificação de Email e telefone de usuários;
* Reset de senhas automáticos;
* Mecanismos de segurança como o MFA;
* É possível associar os usuários a grupos específicos, conforme o acesso que cada um poderá ter;

### 2.1.2.2 É confiável e escalável:

* É totalmente gerenciado pela AWS, com escalabilidade automática. Permitindo o cadastro de incontáveis contas de
  usuários.
* Possui alta disponibilidade, garantindo que o serviço se mantenha ativo, independente de fatores externos.
* Reduz a complexidade no backend. Permitindo melhor qualidade de código e direcionando a preocupação dos
  desenvolvedores para as regras de negócio.
* Possui um acesso gratuito robusto para a realização de testes e Provas de conceito;
* Permite internacionalização;

## 2.1.2.3 Integrações

* Permite a integração nativa com o ecossistema AWS e ferramentas externas como o API Gateway, Lambda, AppSync e entre
  outros;
* Suporta federated login com o Google, Facebook, Apple e entre outros;

## 2.1.2.4 Pontos negativos

* Configuração inicial extensa e complicada;
* Alta curva de aprendizado;
* Debugs e logs me parecem limitados;
* Customização limitada;
* Gestão de usuários via API é engessado, campos personalizados são muito limitados;
* Regeneração de token expira silenciosamente, sendo necessária lógica adicional para revalidação dos tokens;
* Passível de interceptação XSS.
    * Mitigação: Sempre utilizar de tokens curtos, armazenar em mémória ao invés de LocalStorage e sempre utilizar
      HTTPS;
* Refresh Tokens têm vida útil de dias. Se vazarem podem ser utilizados sem muitos prolemas.
    * Mitigação: Utilização de revogação manual e estratégias de armazenamento eficazes;
* Permite regras fracas de senha:
    * Mitigação: Defina uma política robusta de senhas;
* Muitas vezes depende de outros serviços da Amazon como o Amazon WAF ou Lambda Triggers;

## 2.2 Da arquitetura do projeto

* Adicionar a camada de serviços, separando as regras de negócio da camada de controle.
* Criar de classes de DTO para gerir o acesso aos dados;
* Realizar correções nos testes automatizados;
* Configurar base de dados através do Docker-compose. Utilizando das boas práticas:
    * Criação de volume para garantir persistência;
    * Geração de senha segura e randômica com OpenSSL;
    * Configuração de usuário, senha e nome para a base de dados;
* Utilizar de variáveis de ambiente:
    * Garante a não exposição de credenciais sensíveis;
    * Facilita a configuração de deploys e fluxos de CI/CI;
* Incluir de JavaDocs em alguns dos métodos, permitindo acesso facilitado à documentação via IDE;
* Criar a documentação escrita como guia geral da aplicação;

# 3. Alterações Realizadas

* [x] Item 1: Refatoração do código para garantir separação entre camadas de Controle, Serviço e modelo;
* [x] Item 2: Correção dos testes automatizados para suportar a separação das camadas da aplicação;
* [x] Item 3: Configuração do serviço de autenticação na plataforma da AWS;
* [x] Item 4: Configuração das triggers que garantem a atribuição do perfil READER aos novos usuários;
* [x] Item 5: Configuração da integração do projeto Backend com o serviço da AWS;
* [x] Item 6: Criação de lógica de leitura das chaves JWT;
* [x] Item 7: Configuração de permissão de acessos com o Spring Security;
* [x] Item 8: Testes manuais nas APIs realizados com IntelliJ HTTP Client;
* [x] Item 9: Validação dos testes automatizados;
* [x] Item 10: Criação de toda a documentação incluindo JavaDocs;

# 4. Montagem do ambiente de desenvolvimento local

> ⚠️ Não é necessária nenhuma configuração por parte do AWS Cognito. Ele já foi préviamente configurado para a
> utilização nesse desafio.

Pré-requisitos:

* Java 21;
* IDE (Recomendado o Jetbrains IntelliJ);
* Docker;
* IntelliJ HTTP Client (Para acesso aos endpoints);
* Arquivo .env encaminhado por email;
* Navegador atualizado (Para a realização do fluxo de autenticação);

Primeiramente é necessário ter em mãos o arquivo .env recebido por email. Ele conterá todas as variáveis de ambiente
para a execução da aplicação.

Essas variáveis de ambiente devem ser acessíveis aos serviços do Docker, Spring Boot e também deve estar disponível
na hora da execução dos testes automatizados.

Os valores contidos nele, se referem às credenciais do AWS e banco de dados. A senha do banco de dados foi gerada
utilizando o OpenSSL com o seguinte comando:

````shell
  openssl rand -base64 32
````

Após a exportação das variáveis de ambiente, deve-se executar o comando ``docker-compose up --build``, ou pode-se
utilizar dos plugins docker suportados por qualquer IDE.

Após, para executar a aplicação execute o comando:

```sh
  ./gradlew bootRun
```

E para gerar dados fictícios no banco, execute:

```sh
  ./gradlew populateDummyData
```

Para autenticar-se, faça uma chamada pelo navegador
à [http://localhost:8080/api/oauth/authorize](http://localhost:8080/api/oauth/authorize), faça o login ou crie a conta
na hora, confirme o seu email caso necessário e ao ser redirecionado, copie as informações de token em
``/docs/http-tests/config/http-client.private.env.json``. A partir daí, você está autorizado a acessar os endpoints da
aplicação conforme o seu perfil de acesso.

> ⚠️ Esse fluxo foi feito assim apenas para testes. Num ambiente de produção com uma aplicação cliente bem estruturada
> esse redirecionamento é feito automaticamente.

# 3. Implementações futuras

* [ ] Deve-se implementar o serviço de autenticação e validação via número de telefone. Já engatilhado nas configurações
  do AWS Cognito;
* [ ] Deve-se implementar a autenticação multifatorial (MFA) também disponível como recurso no AWS Cognito. Podendo ser
  adicionado sem mais dificuldades;
* [ ] Habilitar o ‘login’ via redes-sociais, Ele pode ser configurado via AWS Cognito levando em consideração a
  configuração específica de cada provedor;
* [ ] Foram configuradas as ferramentas de Internacionalização no backend. Faz-se necessária a implementação dentros das
  classes do sistema. A localização por parte da autenticação já é disponibilizada pelos servições da AWS;
* [ ] Realizar a implementação de testes para os fluxos de Autenticação e autorização;
* [ ] Desenvolver endpoint de ``refresh-token`` e ``logout-sso``;

# 3. Dos outros documentos criados

> ## ⚠️ Atenção!
> Os arquivos .http e os arquivos de configuração para a utilização do IntelliJ Http Client se encontram respectivamente
> em
> ```shell
> /docs/http-tests
> /docs/http-tests/config
> ```

