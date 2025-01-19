# Faroxy v1.0.0 Release Notes

We're excited to announce the first release of Faroxy, a modern HTTP proxy server with real-time traffic monitoring and analysis capabilities!

## 🚀 Features

### HTTP Proxy Server
- Full support for HTTP/HTTPS protocols
- Support for all HTTP methods (GET, POST, PUT, DELETE, etc.)
- Automatic handling of form data and multipart requests
- Custom header forwarding and manipulation
- Query parameter handling and URL rewriting
- Configurable timeout and connection settings

### Real-time Traffic Monitoring
- Modern web interface for live traffic inspection
- Real-time request/response visualization
- Detailed view of headers, parameters, and body content
- Search and filter capabilities
- Response time tracking and performance metrics

### Data Storage and Analysis
- In-memory H2 database for traffic storage
- Persistent logging of all requests and responses
- Searchable traffic history
- Export functionality for logs and traffic data
- Database console access for custom queries

### Developer Tools
- RESTful API for programmatic access
- Customizable configuration options
- Environment-specific settings support
- Local configuration overrides
- Detailed logging and debugging capabilities

### Security Features
- Configurable CORS settings
- Request validation and sanitization
- Error handling and secure logging
- Support for HTTPS traffic

### Integration Support
- Docker container support
- Spring Boot integration
- Gradle-based build system
- Comprehensive API documentation
- Easy-to-use client libraries

## 📚 Documentation
- Complete documentation available at https://vinipx.github.io/faroxy/
- API reference with examples
- Configuration guide
- Tutorial and getting started guides
- Troubleshooting documentation

## 🛠 Technical Details
- Built with Spring Boot 3.2.1
- Requires Java 17 or higher
- Uses H2 database for storage
- Gradle-based build system
- Docker support included

## 🔧 Installation

### Gradle
```gradle
dependencies {
    implementation 'io.faroxy:faroxy:1.0.0'
}
```

### Docker
```bash
docker pull ghcr.io/vinipx/faroxy:1.0.0
```

## 🌟 Getting Started

1. Start the server:
```bash
./gradlew bootRun
```

2. Access the web interface:
```
http://localhost:8080
```

3. Send requests through the proxy:
```bash
curl "http://localhost:8080/proxy?url=https://api.example.com/data"
```

## 🔍 Configuration

Basic configuration in `application.properties`:
```properties
server.port=8080
faroxy.proxy.port=8080
logging.level.io.faroxy=INFO
```

## 🐛 Known Issues
- Large file uploads (>1GB) may require additional memory configuration
- WebSocket support is planned for future releases

## 🔜 Upcoming Features
- WebSocket support
- Advanced request/response transformation
- Traffic replay capabilities
- Extended metrics and analytics
- Performance optimization for large payloads

## 📝 License
This release is available under the MIT License.

## 🙏 Acknowledgments
Special thanks to all contributors who helped make this release possible!

---

For more information, visit our [GitHub repository](https://github.com/vinipx/faroxy) or [documentation site](https://vinipx.github.io/faroxy/).
