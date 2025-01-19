---
layout: default
title: Configuration
nav_order: 4
has_children: true
permalink: /configuration
---

# Configuration Guide

## Overview

Faroxy provides various configuration options to customize its behavior. This guide covers all available configuration options and their usage.

## Configuration Files

### Default Configuration

The default configuration is in `src/main/resources/application.properties`:

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

### Local Configuration

Create a local configuration file for development:

1. Copy the template:
```bash
cp src/main/resources/application-local.properties.template config/application-local.properties
```

2. Customize settings:
```properties
# Server settings
server.port=9090
faroxy.proxy.port=9999

# Logging
logging.level.io.faroxy=DEBUG

# Custom headers
faroxy.proxy.headers.global.X-Proxy=Faroxy
```

## Configuration Properties

### Server Properties

| Property | Description | Default |
|----------|-------------|---------|
| server.port | HTTP server port | 8080 |
| faroxy.proxy.port | Proxy server port | 8080 |

### Database Properties

| Property | Description | Default |
|----------|-------------|---------|
| spring.datasource.url | Database URL | jdbc:h2:mem:testdb |
| spring.datasource.username | Database username | sa |
| spring.datasource.password | Database password | |

### Logging Properties

| Property | Description | Default |
|----------|-------------|---------|
| logging.level.io.faroxy | Log level for Faroxy | INFO |
| logging.file.name | Log file location | |

## Environment Variables

Override any property using environment variables:

```bash
export SERVER_PORT=9090
export SPRING_DATASOURCE_URL=jdbc:h2:mem:customdb
```

## Custom Headers

Configure global headers for all proxied requests:

```properties
faroxy.proxy.headers.global.X-Custom-Header=value
faroxy.proxy.headers.global.X-Proxy=Faroxy
```

## Security Configuration

For production deployments, consider these security settings:

```properties
# HTTPS
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=your-password

# Basic Auth
spring.security.user.name=admin
spring.security.user.password=secure-password
```
