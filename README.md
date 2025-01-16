# Faroxy

The Faroxy is a Spring Boot application that functions as an HTTP web proxy server with request/response logging capabilities.

## Features

- Forward HTTP requests and responses
- Store intercepted requests and responses in H2 in-memory database
- RESTful APIs to retrieve logged requests and responses
- Docker support for easy deployment

## Prerequisites

- Java 17 or higher
- Gradle 7.x or higher
- Docker (optional)

## Building the Application

```bash
./gradlew build
```

## Running Locally

```bash
./gradlew bootRun
```

## Running with Docker

1. Build the Docker image:
```bash
docker build -t faroxy .
```

2. Run the container:
```bash
docker run -p 8080:8080 faroxy
```

## API Usage

### Proxy Endpoint

Send requests through the proxy:
```
POST http://localhost:8080/proxy?targetUrl=https://api.example.com/data
```

### Logging Endpoints

1. Get all logged requests:
```
GET http://localhost:8080/api/requests
```

2. Get all logged responses:
```
GET http://localhost:8080/api/responses
```

### H2 Console

Access the H2 database console at:
```
http://localhost:8080/h2-console
```

JDBC URL: `jdbc:h2:mem:proxydb`
Username: `sa`
Password: (empty)
