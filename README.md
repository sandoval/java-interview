# Interview Project

## Prerequisites
- **Docker** must be installed and running (used for the PostgreSQL database)
- Java 21

## How to Run the Application

### 1. Start the Spring MVC Application
This will start the web application and connect to the database (via Docker Compose):

```sh
./gradlew bootRun
```

### 2. Populate the Database with Dummy Data
To insert at least 10 gateways (serials starting with `ALKG`) and 100 locks (serials starting with `ALKS`), run:

```sh
./gradlew populateDummyData
```

This command runs a special Spring profile (`dummydata`) which loads the dummy data into the database. The web application does **not** start during this process.

### 3. Running Tests

```sh
./gradlew test
```

## Notes
- Ensure Docker is running before starting the application or populating data.
- The database schema is created/updated automatically on startup.
- The application uses PostgreSQL via Docker Compose (see `compose.yaml`).
