# Welcome!

Welcome to our Java coding challenge. This is a challenge for Senior backend
engineer.

We will lay out a problem specification and we expect you to solve this problem
showing your abilities as a Senior engineer.

Keep in mind that for a Senior position it is more important to show the thought
process behind your decisions and your documentation than the implementation itself.

# Problem: Authentication Strategy

## Motivation

Considering the ever-evolving landscape of threat actors, we must take great care
into implementing and updating authentication and authorization strategies to keep
our applications secure.

While commonly needed for every software project, authentication and authorization
are very specific problems with their own pitfalls and requirements. The evolution of
requirements is specially challenging since it greatly depends on factors external to
our organization, such as technological advancements (e.g. passkeys) or evolution of
threat actors and attacks, for example.

## Requirements

### Specifics

The application currently serves multiple API endpoints.
You **must** implement an authentication and authorization strategy for this application.

- Access to all endpoints **must** be authenticated by a previously registered user.
- There **must** be two possible access profiles (read and write).
- Endpoints that make any changes to the database **must** be available only to users with
write access profile.
- Endpoints that only retrieve and display information **must** be available to only to
users with read access profile.

### Universal login (authentication)

There **must** be a central point of authentication for all users and applications.

- It **must** be easy to add new applications for the same user pool.
- Authentication solution **must** support email addresses for user identification.
- Authentication solution **must** support user self-service account creation, requiring
only email verification.
- User accounts **must** have the read access profile automatically assigned upon
account creation.
- Authentication solution **must** support hundreds of thousands of users.
- Authentication user interface **should** be localizable and available at least in
English, Portuguese and Spanish.
- Authentication solution **should** support phone numbers for user identification
in the future.
- Authentication solution **should** support SSO integration in the future.

### Browser-based authentication

Authentication **must** be done on an up-to-date OS-provided browser.

- Browser authentication **should** have bot mitigation strategies in place.

### OAuth2 and JWTs

Authentication and authorization **must** be implemented using OAuth2 and JWTs.

- Authorization **must** use OAuth2 and JSON web tokens (JWTs).


## What we expect

- You **must** choose the best OAuth2 authentication server option between open-source
self-hosted, fully managed (e.g. AWS Cognito, OKTA, Auth0) or self-developed.
- You **must** clearly explain why your choice is the best for these requirements and
explain your thought process behind it, including all information you considered, any
proofs-of-concept you developed for testing, etc.
- You **must** fully understand the security behind the chosen solution, why it is secure,
vectors of attack (if any), how JWTs are secured and verified, etc.
- You **must** present an initial implementation in the form of a pull-request (PR). This
implementation doesn't need to comply with all requirements initially. Any requirements
it doesn't comply with, there **must** be an explanation (plan) on how it will be implemented
in the future.
- You **must** provide instructions in your PR on how to test the solution, including
any Spring configuration parameters, test account and configuration for an API development
environment (we recommend [Hoppscotch](https://docs.hoppscotch.io/documentation/getting-started/introduction)
or [IntelliJ HTTP Client](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html)).
- You **should not** do any frontend work. Validation should be done using an API development
environment.

## Get to work!

- You have five days to complete this challenge. We don't want to take too much of your time.
- Remember that designing the solution, documenting and explaining your decisions is as important
as the actual implementation.
- If you have any questions about the challenge, don't hesitate to contact us at any point.
