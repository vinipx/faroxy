---
layout: default
title: API Reference
nav_order: 3
has_children: true
permalink: /api
---

# API Reference

## Overview

Faroxy provides a RESTful API for programmatic access to proxy functionality and traffic data.

## Base URL

All API endpoints are relative to: `http://localhost:8080`

## Authentication

Currently, the API does not require authentication for local development. For production deployments, consider implementing appropriate security measures.

## Endpoints

### Proxy Endpoint

`POST /proxy`

Proxies an HTTP request to the specified URL.

#### Parameters

| Name | Type | Description |
|------|------|-------------|
| url | string | Target URL to proxy the request to |

#### Example

```bash
curl -X POST "http://localhost:8080/proxy?url=https://api.example.com/data" \
     -H "Content-Type: application/json" \
     -d '{"key": "value"}'
```

### Traffic Logs

`GET /api/requests`

Retrieves logged requests.

#### Query Parameters

| Name | Type | Description |
|------|------|-------------|
| page | integer | Page number (default: 0) |
| size | integer | Page size (default: 20) |
| method | string | Filter by HTTP method |
| status | integer | Filter by status code |

#### Example

```bash
curl "http://localhost:8080/api/requests?method=POST&status=200"
```

### Response Data

`GET /api/responses`

Retrieves logged responses.

#### Query Parameters

| Name | Type | Description |
|------|------|-------------|
| page | integer | Page number (default: 0) |
| size | integer | Page size (default: 20) |
| status | integer | Filter by status code |

#### Example

```bash
curl "http://localhost:8080/api/responses?status=200"
```
