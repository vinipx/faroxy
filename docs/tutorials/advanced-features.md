---
layout: default
title: Advanced Features
parent: Tutorials
nav_order: 2
---

# Advanced Features
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

## Custom Headers

### Adding Global Headers

You can configure Faroxy to add custom headers to all proxied requests:

```properties
faroxy.proxy.headers.global.X-Custom-Header=custom-value
```

### Request-Specific Headers

Add headers to specific requests:

```bash
curl "http://localhost:8080/proxy?url=https://api.example.com/data" \
     -H "X-Custom-Header: custom-value"
```

## Request/Response Manipulation

### Modifying Requests

You can modify requests before they are sent:

```java
@Component
public class CustomRequestInterceptor implements RequestInterceptor {
    @Override
    public void intercept(HttpRequest request) {
        request.addHeader("X-Modified-By", "Faroxy");
    }
}
```

### Response Transformation

Transform responses before returning them:

```java
@Component
public class CustomResponseTransformer implements ResponseTransformer {
    @Override
    public void transform(HttpResponse response) {
        response.addHeader("X-Processed-By", "Faroxy");
    }
}
```

## Traffic Analysis

### Filtering Requests

Use the web interface to filter requests by:
- HTTP Method
- Status Code
- URL Pattern
- Time Range

### Exporting Data

Export traffic logs in various formats:
- JSON
- CSV
- HAR (HTTP Archive)
