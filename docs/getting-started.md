---
layout: default
title: Getting Started
nav_order: 2
---

# Getting Started with Faroxy
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

## Installation

### Prerequisites

Before installing Faroxy, ensure you have:

- Java 17 or higher
- Gradle 7.x or higher
- Docker (optional)

### Quick Install

1. Clone the repository:
```bash
git clone https://github.com/vinipx/faroxy.git
cd faroxy
```

2. Run with Gradle:
```bash
./gradlew bootRun
```

### Docker Installation

1. Build the image:
```bash
docker build -t faroxy .
```

2. Run the container:
```bash
docker run -p 8080:8080 faroxy
```

## Basic Usage

### Accessing the Web Interface

1. Open your browser and navigate to `http://localhost:8080`
2. You'll see the Faroxy dashboard with:
   - Real-time traffic monitor
   - Request/response details
   - Search and filter options

### Using as a Proxy

#### Simple GET Request
```bash
curl "http://localhost:8080/proxy?url=https://api.example.com/data"
```

#### POST Request with JSON
```bash
curl -X POST "http://localhost:8080/proxy?url=https://api.example.com/data" \
     -H "Content-Type: application/json" \
     -d '{"key": "value"}'
```

#### Form Data Submission
```bash
curl -X POST "http://localhost:8080/proxy?url=https://api.example.com/data" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "key1=value1&key2=value2"
```

## Configuration

### Local Configuration

1. Create your local config:
```bash
cp src/main/resources/application-local.properties.template config/application-local.properties
```

2. Edit the configuration:
```properties
server.port=9090
faroxy.proxy.port=9999
logging.level.io.faroxy=DEBUG
```

### Environment Variables

Override settings using environment variables:
```bash
export SERVER_PORT=9090
export SPRING_DATASOURCE_URL=jdbc:h2:mem:customdb
```

## Next Steps

- Learn about [Advanced Features]({% link tutorials/advanced-features.md %})
- Explore the [API Reference]({% link api/index.md %})
- Check out [Configuration Options]({% link configuration/index.md %})
