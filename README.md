# Faroxy

The Faroxy is a Spring Boot application that functions as an HTTP web proxy server with request/response logging capabilities and a JavaFX-based user interface for monitoring traffic.

## Features

- Forward HTTP requests and responses
- Store intercepted requests and responses in H2 in-memory database
- RESTful APIs to retrieve logged requests and responses
- JavaFX-based UI for real-time traffic monitoring
- Cross-platform support (macOS and Windows)
- Docker support for easy deployment

## Prerequisites

- Java 17 or higher
- Gradle 7.x or higher
- Docker (optional)

## Quick Start

### First-Time Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/faroxy.git
cd faroxy
```

2. Run the setup script for your operating system:

For macOS:
```bash
./setup_mac.sh
```

For Windows:
```cmd
setup_windows.bat
```

This will configure your environment and create convenient commands to start and stop the proxy server.

### Starting and Stopping the Proxy

After running the setup script, you can use these commands from anywhere in your terminal:

- Start the proxy server:
```bash
proxystart
```

- Stop the proxy server:
```bash
proxystop
```

### Manual Start/Stop (Alternative Method)

If you prefer not to use the aliases, you can run these scripts directly from the project directory:

For macOS:
```bash
./start.sh  # Start the server
./stop.sh   # Stop the server
```

For Windows:
```cmd
start.bat   # Start the server
stop.bat    # Stop the server
```

## Building the Application

```bash
./gradlew build
```

## Docker Support

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

Configuration:
- JDBC URL: `jdbc:h2:mem:proxydb`
- Username: `sa`
- Password: (empty)

## Troubleshooting

### JavaFX Issues

If you encounter JavaFX-related errors:
1. Ensure you're using Java 17 or higher
2. Try cleaning and rebuilding the project:
```bash
./gradlew clean build
```

### Port Already in Use

If port 8080 is already in use, you can modify the port in `start.sh` (macOS) or `start.bat` (Windows) by changing the `--server.port` argument.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
