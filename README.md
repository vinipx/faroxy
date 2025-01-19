<p align="center">
  <img src="https://raw.githubusercontent.com/vinipx/faroxy/main/docs/assets/images/android-chrome-512x512.png" alt="Faroxy Logo" width="200"/>
</p>

<h1 align="center">Faroxy</h1>

<p align="center">
  A modern HTTP proxy server with real-time traffic monitoring and analysis capabilities
</p>

[![GitHub Release](https://img.shields.io/github/v/release/vinipx/faroxy?style=flat-square&logo=github)](https://github.com/vinipx/faroxy/releases)
[![License](https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square)](LICENSE)
[![GitHub Issues](https://img.shields.io/github/issues/vinipx/faroxy?style=flat-square)](https://github.com/vinipx/faroxy/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/vinipx/faroxy?style=flat-square)](https://github.com/vinipx/faroxy/pulls)
[![Build Status](https://img.shields.io/github/actions/workflow/status/vinipx/faroxy/gradle.yml?branch=main&style=flat-square)](https://github.com/vinipx/faroxy/actions)
[![Last Commit](https://img.shields.io/github/last-commit/vinipx/faroxy?style=flat-square)](https://github.com/vinipx/faroxy/commits/main)
[![Documentation](https://img.shields.io/badge/docs-GitHub%20Pages-blue?style=flat-square&logo=github)](https://vinipx.github.io/faroxy/)

---

<div align="center">

A powerful HTTP proxy server built with Spring Boot, featuring real-time traffic monitoring, request/response logging, and an intuitive web interface.

[Getting Started](#quick-start) •
[Documentation](https://vinipx.github.io/faroxy/) •
[Features](#features) •
[Contributing](#contributing)

</div>

---

## Features

- Forward HTTP requests and responses (GET, POST, and other methods)
- Support for form data and custom headers
- Store intercepted requests and responses in H2 in-memory database
- Web interface for viewing proxy traffic in real-time
- RESTful APIs to retrieve logged requests and responses
- Cross-platform support (macOS and Windows)
- Docker support for easy deployment

## Tech Stack

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg?style=flat-square&logo=spring)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.4-blue?style=flat-square&logo=gradle)](https://gradle.org/)
[![H2](https://img.shields.io/badge/H2-Database-darkblue?style=flat-square&logo=h2)](https://www.h2database.com/)
[![Docker](https://img.shields.io/badge/Docker-Support-blue?style=flat-square&logo=docker)](https://www.docker.com/)

## Prerequisites

- Java 17 or higher
- Gradle 7.x or higher
- Docker (optional)

## Quick Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/vinipx/faroxy.git
   cd faroxy
   ```

2. Run the setup script:
   ```bash
   ./scripts/setup.sh
   ```

   This will:
   - Make the control script executable
   - Add the `faroxy` command to your shell
   - Configure necessary permissions

3. Activate the alias:
   ```bash
   source ~/.zshrc   # if using zsh
   source ~/.bashrc  # if using bash
   ```

4. Start using Faroxy:
   ```bash
   faroxy start    # Start the server
   faroxy status   # Check server status
   faroxy help     # Show all commands
   ```

The application will be available at [http://localhost:8080](http://localhost:8080).

## Running the Application

Faroxy comes with a convenient control script to manage the application. After cloning the repository:

1. Make the control script executable:
   ```bash
   chmod +x scripts/faroxy.sh
   ```

2. Use the script to manage Faroxy:
   ```bash
   # Start the server
   ./scripts/faroxy.sh start

   # Stop the server
   ./scripts/faroxy.sh stop

   # Restart the server
   ./scripts/faroxy.sh restart

   # Check server status
   ./scripts/faroxy.sh status

   # Show help
   ./scripts/faroxy.sh help
   ```

3. Optional: Add an alias to your shell configuration (e.g., `.bashrc` or `.zshrc`):
   ```bash
   echo 'alias faroxy="$HOME/path/to/faroxy/scripts/faroxy.sh"' >> ~/.zshrc
   source ~/.zshrc
   ```

   Then you can use `faroxy` command from anywhere:
   ```bash
   faroxy start
   faroxy status
   ```

## Using the Proxy

### Web Interface

Access the web interface at `http://localhost:8080` to:
- View all proxy traffic in real-time
- Filter and search through requests/responses
- Inspect request/response headers and bodies

### API Usage

1. Send requests through the proxy:

```bash
# GET request
curl "http://localhost:8080/proxy?url=https://api.example.com/data"

# POST request with JSON body
curl -X POST "http://localhost:8080/proxy?url=https://api.example.com/data" \
     -H "Content-Type: application/json" \
     -d '{"key": "value"}'

# POST request with form data
curl -X POST "http://localhost:8080/proxy?url=https://api.example.com/data" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "key1=value1&key2=value2"

# Request with custom headers
curl "http://localhost:8080/proxy?url=https://api.example.com/data" \
     -H "X-Custom-Header: custom-value"
```

2. View logged requests/responses:
```bash
# Get all requests
curl http://localhost:8080/api/requests

# Get all responses
curl http://localhost:8080/api/responses
```

## Documentation

For detailed documentation, tutorials, and API reference, visit our [Documentation Site](https://vinipx.github.io/faroxy/).

The documentation includes:
- Getting Started Guide
- API Reference
- Configuration Options
- Advanced Features
- Troubleshooting Guide

## Configuration

### Local Development Configuration

For local development, you can create your own configuration file that won't be tracked by Git:

1. Copy the template file:
```bash
cp src/main/resources/application-local.properties.template config/application-local.properties
```

2. Edit `config/application-local.properties` with your preferred settings:
```properties
# Example local configuration
server.port=9090
faroxy.proxy.port=9999
logging.level.io.faroxy=DEBUG
```

The application will:
1. First load the default settings from `application.properties`
2. Then override them with your local settings from `config/application-local.properties` if it exists
3. Finally, apply any environment variables or system properties

### Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Environment Variables

You can override configuration using environment variables:

```bash
export SERVER_PORT=9090
export SPRING_DATASOURCE_URL=jdbc:h2:mem:customdb
```

## Development

### Building the Project

```bash
./gradlew clean build
```

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests io.faroxy.integration.ProxyIntegrationTest
```

### Database Console

Access the H2 database console at `http://localhost:8080/h2-console`:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   export SERVER_PORT=9090
   ./gradlew bootRun
   ```

2. **Database Issues**
   - Ensure H2 console is accessible at `http://localhost:8080/h2-console`
   - Check application logs for database-related errors
   - Try clearing the in-memory database by restarting the application

## Contributing

We love your input! We want to make contributing to Faroxy as easy and transparent as possible, whether it's:

- Reporting a bug
- Discussing the current state of the code
- Submitting a fix
- Proposing new features
- Becoming a maintainer

Read our [Contributing Guidelines](CONTRIBUTING.md) to get started.

### Development Process

We use GitHub to host code, to track issues and feature requests, as well as accept pull requests.

1. Fork the repo and create your branch from `main`
2. If you've added code that should be tested, add tests
3. If you've changed APIs, update the documentation
4. Ensure the test suite passes
5. Make sure your code follows the existing style
6. Issue that pull request!

## Stats

![GitHub Stats](https://github-readme-stats.vercel.app/api?username=vinipx&show_icons=true&theme=radical)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

Made with ❤️ by [vinipx](https://github.com/vinipx)

</div>
