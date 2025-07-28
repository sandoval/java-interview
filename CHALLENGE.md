# Welcome!

Welcome to our Java coding challenge. This is a challenge for Senior software
developers.

We will lay out a problem specification and we expect you to solve this problem
showing your abilities as a Senior engineer.

Keep in mind that for a Senior position it is more important to show the thought
process behind your decisions and your documentation than the implementation itself.

# Problem: Authentication Strategy

## Motivation

Considering the ever-evolving landscape of threat actors, we **must** take great care
into implementing and updating authentication and authorization strategies to keep
our applications secure.

While commonly needed for every software project, authentication and authorization
are very specific problems with their own pitfalls and requirements. The evolution of
requirements is specially challenging since it greatly depends on factors external to
our organization, such as technological advancements (e.g. passkeys) or evolution of
threat actors and attacks, for example.

## Requirements

### Universal login (authentication)

There **must** be a central point of authentication for all users and applications.

- Single implementation of authentication logic to make sure all rules are followed
by all applications.
- Single service access to user credentials, making it easier to protect them.
- Changes to the authentication process may be done without interfering with any clients.
- **must** be easy to add new applications for the same user pool.

### Browser-based authentication

Authentication **must** be done on an up-to-date OS-provided browser.

- Users are educated to only provide their credentials on a browser to the authenticated
domain.
- Authentication experience will be uniform across all clients (web, mobile apps, etc).
- Easier to implement bot and DDoS mitigation strategies.
- Most up-to-date technologies are first available on browsers (e.g. passkeys).

### OAuth2

Authorization **must** be implemented using OAuth2.

- Battle-tested and widely used standard for authorization.
- Enables several authorization flows for different types of clients, such as confidential
(e.g. third-party APIs), public (e.g. web, mobile apps) and ones with limited input
(e.g. hardware devices).
- Allows for limiting the scope of the authorization according to the client or user.
- Allows for refreshing credentials for long-lived sessions.

### JSON Web Tokens (JWTs)

Authorization **must** be implemented using JWTs.

- Token validity may be verified locally with cryptography (no network requests required).

### Other requirements

- Authentication user interface **must** be localizable and available at least in
English, Portuguese and Spanish.
- Authentication solution **must** support both email addresses and phone numbers
as methods for user identification.
- Authentication solution **should** support SSO integration.
- Authentication solution **must** provide bot-mitigation tools.
- Authentication solution **must** support hundreds of thousands of users.
- There **must** be a way of restricting what any user can do in the platform,
for example restricting some users to only read from the platform and others
to read or write to the platform.

## What we expect

- You **must** choose the best OAuth2 authentication server option between open-source
self-hosted, fully managed (e.g. AWS Cognito, OKTA, Auth0) or self-developed.
- You **must** clearly explain why your choice is the best for these requirements and
explain your thought process behind it, including all information you considered, any
proofs-of-concept you developed for testing.
- You **must** fully understand the security behind the chosen solution, why it is secure,
vectors of attack (if any), how JWTs are secured and verified, etc.
- You **should** present an initial implementation in the form of a pull-request (PR). This
implementation doesn't need to comply with all requirements initially. Any requirements
it doesn't comply with, there **must** be an explanation (plan) on how it will be implemented
in the future.

## Get to work!

- You have five days to complete this challenge. We don't want to take much of your time.
- Remember that designing the solution, documenting and explaining it is more important
than the actual implementation. If you don't have time to do it all, focus on the design.
- If you have any questions about the challenge, don't hesitate to contact us at any point.
