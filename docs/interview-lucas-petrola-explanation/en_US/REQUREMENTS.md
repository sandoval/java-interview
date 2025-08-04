# ASSA ABLOY CHALLENGE Requirements Document

## 1. Introduction

# 1.1 Purpose of the Requirements Document

This document specifies the characteristics and functionalities of the project, which is a challenge that simulates the
implementation of
access control for locks
and gateways in an IoT system.

## 1.2 Product Scope

It was developed as a REST API, which allows the retrieval and editing of information on locks,
entrance doors, and access links. It should be able to access, retrieve, and edit
information on user access locks.

## 1.3 Overview of the Rest of the Document

* Section 2 contains a description of the product's perspectives, its functionalities,
  general restrictions, assumptions, and dependencies.

* Section 3 presents the functional, non-functional, and internal interface requirements.

# 2. General Description

## 2.1 Product Overview

The system aims to facilitate the monitoring, editing, modification, and retrieval of information regarding the status
of locks,
gates, and
links connected to an IoT system.

> The system will simulate access with external peripherals.

## 2.2 Product Functions

The system must allow for the monitoring, insertion, and updating of data from these locks, gates, and links. There must
be
access control according to each user's profile.

## 2.3 User Characteristics

The users of the system will be the end consumers, who must be familiar with technology in order to
use the system.

## 2.4 General Restrictions

Access to all terminals must be authenticated by a previously registered user.
There will be two possible access profiles: “Read” and “Write.” Terminals that
make changes to the database will only be available to users with a
``write access`` profile. Terminals that only retrieve and display information will be
available only to users with a ``read access`` profile.

## 2.5 Assumptions and Dependencies

The system was designed to use the latest version of the PostgreSQL database
in a Docker container, via Docker Compose, and implemented a persistence layer
using JPA (Spring Data).

Authentication was developed using OAuth2 with AWS Cognito.

For authorization, JWT was implemented to generate access tokens with
data release in the token itself and expiration dates for the tokens.

# 3. Specific Requirements

## 3.1.1 Basic Authentication Functions

* **RF_B1**: There must be a central authentication point for all users and applications,
  making it easy to add new applications to the same user group.
* **RF_B2**: The authentication solution must support email addresses for user identification
  .
* **RF_B3**: The authentication solution must support the creation of self-service accounts by the
  user, requiring only email verification.
* **RF_B4**: User accounts must have the read access profile automatically assigned
  at the time of account creation.
* **RF_B5**: The authentication solution must support hundreds of thousands of users.
* **RF_B6**: The authentication user interface must be localizable and available in at least
  English, Portuguese, and Spanish.
* **RF_B7**: The authentication solution must support phone numbers for user identification
  in the future.
* **RF_B8**: The authentication solution must support SSO integration in the future.
* **RF_B9**: Authentication must be performed in an up-to-date browser provided by the operating system.

## 3.1.2 Fundamental Functions

* **RF_F1**: The system must allow consultation of installed gateways and display their status;
* **RF_F2**: The system must allow consultation of installed locks and display their status;
* **RF_F3**: The system must allow consultation of installed locks and gateways, and display their integration and
  status;

## 3.2 Quality Requirements

### 3.2.1 Functionality

* **RF_QF 1.1**: The system contains functions to facilitate the monitoring of lock systems, gates, and their
  integrations.
* **RF_QF 1.2**: To access the system, the user is uniquely identified, thus allowing
  the system to protect information and provide it only to authorized persons.

### 3.2.2 Usability

* **RF_QU 1.1**: The system must be easy to use and can be learned by any user familiar with
  technology.

### 3.2.3 Efficiency

* **RF_QE 1.1**: Response time to queries must be fast, with no latency exceeding 3 seconds.

## 3.3 External Interface Requirements

### 3.3.1 User Interfaces

> All authentication flows will be performed through the backend, a browser, and HTTP solutions.