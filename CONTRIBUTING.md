# Contributing to Faroxy

First off, thank you for considering contributing to Faroxy! This document provides guidelines and best practices for contributing to make the process smooth for everyone.

## Table of Contents
- [Code of Conduct](#code-of-conduct)
- [Development Workflow](#development-workflow)
- [Commit Guidelines](#commit-guidelines)
- [Testing Requirements](#testing-requirements)
- [Documentation](#documentation)
- [Pull Request Process](#pull-request-process)

## Code of Conduct

We are committed to providing a friendly, safe, and welcoming environment for all contributors. Please read and follow our [Code of Conduct](CODE_OF_CONDUCT.md).

## Development Workflow

1. **Before Starting**
   - Check existing issues and PRs
   - Discuss major changes in an issue first
   - Read the documentation

2. **While Developing**
   - Write clean, documented code
   - Follow code standards
   - Add tests for new features
   - Update documentation

3. **Before Submitting**
   - Run all tests locally
   - Update changelog if needed
   - Squash related commits

## Commit Guidelines

### Commit Message Format
```
<type>(<scope>): <subject>

[optional body]

[optional footer(s)]
```

### Types
| Type | Description |
|------|-------------|
| feat | New feature |
| fix | Bug fix |
| docs | Documentation changes |
| style | Formatting, missing semicolons, etc. |
| refactor | Code restructuring |
| perf | Performance improvements |
| test | Adding or updating tests |
| chore | Maintenance tasks |
| ci | CI/CD changes |

### Examples
✅ Good:
```bash
feat(proxy): add support for WebSocket connections

- Implement WebSocket handshake
- Add connection pooling
- Update documentation

Closes #123
```

❌ Bad:
```bash
fixed stuff
```

## Testing Requirements

### Test Structure
```java
@SpringBootTest
class ProxyServiceTest {
    @Test
    void shouldHandleProxyRequest_WhenValidRequest_ThenSuccess() {
        // Given
        ProxyRequest request = createValidRequest();
        
        // When
        ProxyResponse response = service.handle(request);
        
        // Then
        assertThat(response)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.getStatus()).isEqualTo(200);
                assertThat(r.getBody()).isNotEmpty();
            });
    }
}
```

### Coverage Requirements
- Unit Tests: 80% minimum coverage
- Integration Tests: All endpoints covered
- Performance Tests: Critical paths tested

## Documentation

1. **Code Documentation**
   ```java
   /**
    * Handles incoming proxy requests.
    *
    * @param request the proxy request containing target URL and headers
    * @return the proxy response with status and body
    * @throws ProxyException if the request fails or times out
    */
   public ProxyResponse handle(ProxyRequest request) {
       // Implementation
   }
   ```

2. **API Documentation**
   - Use OpenAPI/Swagger annotations
   - Include request/response examples
   - Document error responses

3. **README Updates**
   - Keep features list current
   - Update configuration examples
   - Maintain troubleshooting guide

## Pull Request Process

1. **PR Title Format**
   ```
   type(scope): brief description
   ```

2. **PR Description Template**
   ```markdown
   ## Description
   Clear explanation of the changes
   
   ## Type of Change
   - [ ] Bug fix
   - [ ] New feature
   - [ ] Breaking change
   - [ ] Documentation update
   
   ## Testing
   - [ ] Unit tests added/updated
   - [ ] Integration tests added/updated
   - [ ] Manual testing performed
   
   ## Documentation
   - [ ] JavaDoc updated
   - [ ] README updated
   - [ ] API docs updated
   
   ## Additional Notes
   Any extra information
   ```

3. **Review Requirements**
   - One approval required
   - All CI checks passing
   - All discussions resolved
   - No merge conflicts

---

Remember: Quality over quantity. Take your time to write good code that others can understand and maintain.
