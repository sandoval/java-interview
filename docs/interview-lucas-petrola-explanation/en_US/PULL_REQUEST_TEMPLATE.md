# Description

## 1. Problem

### 1.1 Lack of authentication in the original project

The project did not have any authentication or authorization mechanism implemented.
All routes were public, which represents a high risk for any application that handles sensitive data or
allows access control to features that could be harmful to the end user's security if it falls into
the wrong hands.

### 1.1.2 Consequences of the absence of authentication:

* Anyone could consume the API endpoints, regardless of whether they were a legitimate user;
* It was not possible to manage access permissions, as there was no user identification;
* There were no mechanisms for expiring and/or revoking user access, allowing total control of the application
  for indefinite periods;
* Any data exposed by the API was vulnerable to misuse by robots and other means of automation;

### 1.2 Project architecture

The project did not have separation of responsibilities in some layers of the application.

* There was no DTO model, where the user was shown exactly what was in the entities;
* The responsibility of the service layer was being implemented within the control classes;
* No internationalization was configured. This allowed for the presentation of only one language;
* Some tests were not passing;
* Incomplete database configuration via Docker-compose;
* There was no system documentation;

## 2. Solution

### 2.1 Implementation of authentication with AWS Cognito + JWT

To resolve this issue, an OAuth2 Authorization Code flow was implemented with AWS Cognito
as the identity provider. Authentication is now based on JWT tokens, which enable:

* Secure verification of user identity in each request via ``access_token``;
* Extraction of authenticated user data from the token;
* Automatic session expiration control;
* Mapping of access profiles based on ``AWS Cognito`` groups;

### 2.1.2 Option for AWS Cognito

AWS Cognito is a service offered by Amazon Web Services (AWS) that provides the entire flow of authentication,
authorization, and management of accounts and user groups. It allows for the implementation of secure login without the
need to
build an entire infrastructure from scratch.

It supports various security standards such as OAuth2, OpenID Connect, and SAML 2.0. The tokens issued can be
used in APIs protected with JWT.

The documentation is robust and there is extensive community support. It is a stable, reliable, and scalable solution.
It is
one of the most popular solutions on the market. It offers a range of feature options and excellent market acceptance.

### 2.1.2.1 It provides user pools with ready authentication, which include:

* Login with email, phone number, and password;
* User email and phone number verification tools;
* Automatic password resets;
* Security mechanisms such as MFA;
* Users can be associated with specific groups, according to the access each one may have;

### 2.1.2.2 It is reliable and scalable:

* It is fully managed by AWS, with automatic scalability. Allowing the registration of countless user accounts
  .
* It has high availability, ensuring that the service remains active, regardless of external factors.
* It reduces backend complexity. This allows for better code quality and directs developers' attention
  to business rules.
* It has robust free access for testing and proof of concept;
* It allows for internationalization;

## 2.1.2.3 Integrations

* Allows native integration with the AWS ecosystem and external tools such as API Gateway, Lambda, AppSync, and others
  ;
* Supports federated login with Google, Facebook, Apple, and others;

## 2.1.2.4 Negative points

* Extensive and complicated initial configuration;
* Steep learning curve;
* Debugs and logs seem limited to me;
* Limited customization;
* User management via API is rigid, custom fields are very limited;
* Token regeneration expires silently, requiring additional logic to revalidate tokens;
* Susceptible to XSS interception.
    * Mitigation: Always use short tokens, store them in memory instead of LocalStorage, and always use
      HTTPS;
* Refresh tokens have a lifespan of days. If they leak, they can be used without much trouble.
    * Mitigation: Use manual revocation and effective storage strategies;
* Allows weak password rules:
* Mitigation: Set a robust password policy;
* Often depends on other Amazon services such as Amazon WAF or Lambda Triggers;

## 2.2 Project architecture

* Add the service layer, separating business rules from the control layer.
* Create DTO classes to manage data access.
* Make corrections to automated tests.
* Configure the database using Docker-compose. Using best practices:
    * Create volume to ensure persistence;
* Generate secure and random passwords with OpenSSL;
* Configure user, password, and name for the database;
* Use environment variables:
* Ensures sensitive credentials are not exposed;
* Facilitates the configuration of deploys and CI/CI flows;
* Include JavaDocs in some of the methods, allowing easy access to documentation via IDE;
* Create written documentation as a general guide to the application;

# 3. Changes Made

* [x] Item 1: Refactoring of code to ensure separation between Control, Service, and Model layers;
* [x] Item 2: Correction of automated tests to support the separation of application layers;
* [x] Item 3: Configuration of the authentication service on the AWS platform;
* [x] Item 4: Configuration of triggers that ensure the READER profile is assigned to new users;
* [x] Item 5: Configuration of the Backend project integration with the AWS service;
* [x] Item 6: Creation of JWT key reading logic;
* [x] Item 7: Configuration of access permissions with Spring Security;
* [x] Item 8: Manual testing of APIs performed with IntelliJ HTTP Client;
* [x] Item 9: Validation of automated tests;
* [x] Item 10: Creation of all documentation, including JavaDocs;

# 4. Setting up the local development environment

> ⚠️ No configuration is required on the part of AWS Cognito. It has already been preconfigured for
> use in this challenge.

Prerequisites:

* Java 21;
* IDE (Jetbrains IntelliJ recommended);
* Docker;
* IntelliJ HTTP Client (for access to endpoints);
* .env file sent by email;
* Updated browser (for authentication flow);

First, you need to have the .env file received by email. It will contain all the environment variables
for running the application.

These environment variables must be accessible to Docker and Spring Boot services and must also be available
when running automated tests.

The values contained in it refer to AWS and database credentials. The database password was generated
using OpenSSL with the following command:

````shell
  openssl rand -base64 32
````

After exporting the environment variables, run the command ``docker-compose up --build``, or you can
use the docker plugins supported by any IDE.

Then, to run the application, execute the command:

```sh
  ./gradlew bootRun
```

And to generate dummy data in the database, execute:

```sh
  ./gradlew populateDummyData
```

To authenticate, make a call through your browser
to [http://localhost:8080/api/oauth/authorize](http://localhost:8080/api/oauth/authorize), log in or create an account
on the spot, confirm your email if necessary, and when redirected, copy the token information in
``/docs/http-tests/config/http-client.private.env.json``. From there, you are authorized to access the application
endpoints
according to your access profile.

> ⚠️ This flow was created for testing purposes only. In a production environment with a well-structured client
> application,
> this redirection is done automatically.

# 3. Future implementations

* [ ] The authentication and validation service via phone number should be implemented. Already triggered in the AWS
  Cognito settings;
* [ ] Multifactor authentication (MFA) should also be implemented as a feature in AWS Cognito. It can be
* [ ] Multifactor authentication (MFA) should also be implemented, which is available as a feature in AWS Cognito. It
  can be added without further difficulties;
* [ ] Enable ‘login’ via social networks. It can be configured via AWS Cognito, taking into account the
  specific configuration of each provider;
* [ ] Internationalization tools have been configured in the backend. Implementation within the
  system classes is necessary. Localization for authentication is already provided by AWS services;
* [ ] Perform implementation tests for authentication and authorization flows;
* [ ] Develop `refresh-token` and `logout-sso` endpoints;

# 3. Other documents created

> ## ⚠️ Attention!
> The .http files and configuration files for using IntelliJ Http Client are located respectively
> in
> ```shell
> /docs/http-tests
> /docs/http-tests/config
> ```