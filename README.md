# Library Management System

### System built with Spring Boot, JPA, and Docker. The application allows managing authors, books, members, and loans.

## Build

To clean and build the project without running tests, execute:

```shell
./gradlew clean build -x test
```

## Test

To run the tests, execute:

```shell
./gradlew test
```

## Deployment

To deploy the application using Docker Compose, execute:

```shell
docker compose --file docker-compose.yml --project-name library up --build -d
```

## API Documentation

You can access the API documentation at:

```yaml
URL: http://localhost:8080/swagger-ui/index.html
```