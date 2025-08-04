# Challenge Requirements - Senior Backend Developer

These are all the topics for approval in the challenge for Senior Backend Developers.

The application currently serves multiple API endpoints. You must implement an authentication and authorization strategy
for this application.

---

Specific

## Specifics

Access to all terminals must be authenticated by a previously registered user.

* [x] There must be two possible access profiles (read and write).
* [x] Endpoints that make changes to the database must only be available to users with a
  write access profile.
* [x] Endpoints that only retrieve and display information must only be available to users with a
  read access profile.

## Authentication

There should be a central authentication point for all users and applications.

* [x] It should be easy to add new applications to the same user group.
* [x] The authentication solution should support email addresses for user identification.
* [x] The authentication solution must support self-service account creation by the user, requiring only
  email verification.
* [x] User accounts must have the read access profile automatically assigned at the time of account creation
  .
* [x] The authentication solution must support hundreds of thousands of users.
* [x] The authentication user interface must be localizable and available in at least English, Portuguese, and
  Spanish.
* [x] The authentication solution must support phone numbers for user identification in the future.
* [x] The authentication solution must support SSO integration in the future.
* [x] Authentication must be performed in an up-to-date browser provided by the operating system.

## OAuth2 and JWTs

* [x] Authentication and authorization must be implemented using OAuth2 and JWTs.
* [x] Authorization must use OAuth2 and JSON Web Tokens (JWTs).

