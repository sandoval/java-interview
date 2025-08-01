# Lock Simulator

This is an example project used for Java coding interviews.

This system provides a simple implementation of a lock simulator. It virtually emulates
locks and gateways in an IoT system.

## Prerequisites
- **Docker** must be installed and running (used for the PostgreSQL database)
- Java 21 or 24

## How to Run the Application

### 0. Configure enviromnent 

Use the environment variables in java.env.example in the IntelliJ SpringBoot service to perform the build. It can be found in the forwarded email.

You also need to export the values from ``docker.env.example`` in the Docker service. This will generate the database environment with the correct configuration. 

If you want to generate a new secure password for Postgres, use the command:

```shell
 openssl rand -base64 32
```

### 1. Start the Spring MVC Application
This will start the web application and connect to the database (via Docker Compose):

```sh
./gradlew bootRun
```

### 2. Populate the Database with Dummy Data
To insert at least 10 gateways (serials starting with `ALKG`) and 100 locks (serials
starting with `ALKS`), run:

```sh
./gradlew populateDummyData
```

This command runs a special Spring profile (`dummydata`) which loads the dummy data
into the database. The web application does **not** start during this process.

Make sure you stop the previous bootRun command before running this command. This
command doesn't exit automatically, make sure you quit (Ctrl+C) after the application loads.

### 3. Running Tests

```sh
./gradlew test
```

## Notes
- Ensure Docker is running before starting the application or populating data.
- The database schema is created/updated automatically on startup.
- The application uses PostgreSQL via Docker Compose (see `compose.yaml`).
