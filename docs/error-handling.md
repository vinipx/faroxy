# Error Handling in Faroxy

This document details how Faroxy handles various error scenarios and provides guidance for troubleshooting common issues.

## Error Types

### Network Errors

#### Connection Refused
When a target server actively refuses the connection:
- Error Message: "Connection refused: Unable to connect to the target server"
- Common Causes:
  - Target server is down
  - Incorrect port number
  - Firewall blocking the connection

#### Premature Connection Closure
When the server closes the connection unexpectedly:
- Error Message: "Connection closed prematurely by the target server"
- Common Causes:
  - Server timeout
  - Network instability
  - Server-side issues

### URL Errors

#### Invalid URL Format
When the provided URL is malformed:
- Error Message: "Invalid URL format: [specific error details]"
- Common Causes:
  - Missing protocol (http/https)
  - Invalid characters in URL
  - Malformed query parameters

### Server Errors

#### HTTP Status Errors (4xx, 5xx)
When the target server returns an error status:
- Error Message: Full response body from the server
- Common Status Codes:
  - 404: Resource not found
  - 403: Forbidden
  - 500: Internal server error
  - 502: Bad gateway
  - 504: Gateway timeout

## Error Handling Features

### 1. Connection Pool Management
- Maximum connections limited to 50
- Connection timeout settings
- Automatic connection cleanup
- SSL context configuration

### 2. WebSocket Error Handling
- Automatic reconnection attempts
- Real-time status updates
- Error message broadcasting
- Connection state monitoring

### 3. Request/Response Logging
- All errors are logged with:
  - Timestamp
  - Error type
  - Stack trace (when applicable)
  - Request details
  - Response details (if available)

### 4. Web Interface Integration
- Visual error indicators
- Detailed error messages
- Connection status display
- Real-time updates

## Troubleshooting Guide

### Common Issues and Solutions

1. **Connection Refused**
   - Check if the target server is running
   - Verify the port number
   - Check firewall settings

2. **SSL/TLS Errors**
   - Verify the target server's SSL certificate
   - Check if HTTPS is required
   - Ensure proper SSL context configuration

3. **Timeout Issues**
   - Adjust connection timeout settings
   - Check network stability
   - Verify server response times

4. **Invalid URLs**
   - Ensure proper URL format
   - Include protocol (http/https)
   - URL encode special characters

## API Error Responses

All API error responses follow this format:
```json
{
  "error": {
    "code": "ERROR_CODE",
    "message": "Human readable error message",
    "details": {
      "timestamp": "2025-01-26T14:21:45-05:00",
      "requestId": "uuid",
      "additionalInfo": "..."
    }
  }
}
```

## Error Monitoring and Analysis

### Logging
- All errors are logged using SLF4J
- Log levels: ERROR for failures, WARN for potential issues
- Structured logging format for easy parsing

### Metrics
- Error rate tracking
- Response time monitoring
- Connection pool statistics
- WebSocket connection status

## Best Practices

1. **URL Handling**
   - Always validate URLs before sending
   - Use proper URL encoding
   - Include protocol (http/https)

2. **Connection Management**
   - Monitor connection pool usage
   - Implement proper timeouts
   - Handle connection cleanup

3. **Error Recovery**
   - Implement retry mechanisms
   - Provide clear error messages
   - Log sufficient context for debugging
