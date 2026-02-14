# Logging Structure - Filae API

## Overview

Comprehensive logging system providing full visibility across all endpoints with consistent patterns and formatting.

---

## Features

✅ **HTTP Request/Response Logging** - Every request is logged with timing  
✅ **Colored Console Output** - ANSI colors for better readability  
✅ **File Logging** - Rotating log files in `logs/` directory  
✅ **Request ID Tracking** - UUID per request for correlation  
✅ **Performance Metrics** - Duration tracking for each request  
✅ **Exception Logging** - Global exception handler with context  
✅ **Security Events** - Authentication and authorization logging  
✅ **Validation Logging** - Input validation errors tracked  

---

## Log Format

### Console Output (with colors)
```
2026-02-14 22:30:15.123  INFO 12345 --- [nio-8080-exec-1] c.f.a.i.logging.LoggingInterceptor : ┌─ [a1b2c3d4] GET /api/establishments - Client: 192.168.1.100
2026-02-14 22:30:15.125 DEBUG 12345 --- [nio-8080-exec-1] c.f.a.a.c.EstablishmentController : → Entering findAll()
2026-02-14 22:30:15.150 DEBUG 12345 --- [nio-8080-exec-1] c.f.a.a.c.EstablishmentController : ← Exiting findAll() - Result: 5 establishments
2026-02-14 22:30:15.152  INFO 12345 --- [nio-8080-exec-1] c.f.a.i.logging.LoggingInterceptor : └─ [a1b2c3d4] GET /api/establishments - Status: 200 ✓ - Duration: 29ms
```

### File Output
```
2026-02-14 22:30:15.123 INFO 12345 --- [nio-8080-exec-1] c.f.a.i.logging.LoggingInterceptor : ┌─ [a1b2c3d4] GET /api/establishments - Client: 192.168.1.100
2026-02-14 22:30:15.152 INFO 12345 --- [nio-8080-exec-1] c.f.a.i.logging.LoggingInterceptor : └─ [a1b2c3d4] GET /api/establishments - Status: 200 ✓ - Duration: 29ms
```

---

## Components

### 1. LoggingInterceptor
**Location:** `com.filae.api.infrastructure.logging.LoggingInterceptor`

Intercepts all HTTP requests and logs:
- Request method, URI, query params
- Client IP address (handles X-Forwarded-For)
- Response status code
- Request duration in milliseconds
- Unique request ID (UUID)

**Status Indicators:**
- `✓` - Success (2xx)
- `➜` - Redirect (3xx)
- `⚠` - Client error (4xx)
- `✗` - Server error (5xx)

### 2. LogHelper
**Location:** `com.filae.api.infrastructure.logging.LogHelper`

Utility class for consistent logging patterns:

```java
// Method entry/exit
LogHelper.logMethodEntry(log, "methodName", param1, param2);
LogHelper.logMethodExit(log, "methodName", result);

// Business operations
LogHelper.logOperation(log, "Creating establishment", "name=" + name);

// Errors
LogHelper.logError(log, "save establishment", exception, "id=" + id);

// Validation
LogHelper.logValidationError(log, "email", "Invalid format");

// Database operations
LogHelper.logDatabaseOperation(log, "INSERT", entityId);

// Security events
LogHelper.logSecurityEvent(log, "Login success", username);
```

### 3. GlobalExceptionHandler
**Location:** `com.filae.api.infrastructure.exception.GlobalExceptionHandler`

Catches and logs all exceptions with proper HTTP responses:
- Validation errors (400)
- Authentication failures (401)
- Access denied (403)
- Not found (404)
- Internal errors (500)

### 4. WebConfig
**Location:** `com.filae.api.infrastructure.config.WebConfig`

Registers the logging interceptor for all endpoints (except Swagger).

---

## Configuration

### application.yml

```yaml
logging:
  pattern:
    console: "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p ${PID:- } --- [%t] %-40.40logger{39} : %m%n%wEx"
  file:
    name: logs/filae-api.log
    max-size: 10MB
    max-history: 30
  level:
    root: INFO
    com.filae.api: DEBUG
    com.filae.api.infrastructure.logging: DEBUG
    org.springframework.web: INFO
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG

spring:
  output:
    ansi:
      enabled: ALWAYS  # Enable colors
```

### Suppressing JVM Warnings

The application is configured to suppress Java native access warnings from Tomcat. These are configured in:

1. **pom.xml** - Spring Boot Maven plugin includes JVM arguments
2. **.mvn/jvm.config** - For Maven wrapper execution
3. **IntelliJ Run Configuration** - Add to VM options:
```
--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED
```

---

## Usage in Controllers

### Basic Pattern

```java
@RestController
@RequestMapping("/api/example")
public class ExampleController {
    
    private static final Logger log = LogHelper.getLogger(ExampleController.class);
    
    @GetMapping
    public ResponseEntity<List<Example>> getAll() {
        LogHelper.logMethodEntry(log, "getAll");
        
        try {
            LogHelper.logOperation(log, "Fetching all examples");
            List<Example> examples = service.findAll();
            
            LogHelper.logMethodExit(log, "getAll", examples.size() + " items");
            return ResponseEntity.ok(examples);
            
        } catch (Exception e) {
            LogHelper.logError(log, "getAll", e);
            throw e;
        }
    }
    
    @PostMapping
    public ResponseEntity<Example> create(@Valid @RequestBody ExampleDTO dto) {
        LogHelper.logMethodEntry(log, "create", dto.getName());
        
        try {
            LogHelper.logOperation(log, "Creating example", "name=" + dto.getName());
            Example example = service.create(dto);
            LogHelper.logDatabaseOperation(log, "INSERT", example.getId());
            
            LogHelper.logMethodExit(log, "create", example.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(example);
            
        } catch (Exception e) {
            LogHelper.logError(log, "create", e, "name=" + dto.getName());
            throw e;
        }
    }
}
```

---

## Log Levels

| Level | Purpose | Example |
|-------|---------|---------|
| **ERROR** | System errors, exceptions | Server crashes, database failures |
| **WARN** | Potential issues | Validation failures, deprecated usage |
| **INFO** | Important events | Request/response, business operations |
| **DEBUG** | Detailed flow | Method entry/exit, database queries |
| **TRACE** | Very detailed | SQL parameters, full stack traces |

---

## Log Files

Logs are written to:
```
logs/
  └── filae-api.log         # Current log file
  └── filae-api.log.1       # Rotated log (yesterday)
  └── filae-api.log.2       # Older log
  └── ...
```

**Rotation:**
- Max file size: 10MB
- Max history: 30 files
- Automatic compression of old files

---

## Example Logs

### Successful Request
```
2026-02-14 22:30:15.123  INFO --- [nio-8080-exec-1] LoggingInterceptor : ┌─ [a1b2c3d4] GET /api/health - Client: 127.0.0.1
2026-02-14 22:30:15.125 DEBUG --- [nio-8080-exec-1] HealthCheckController : → Entering healthCheck()
2026-02-14 22:30:15.127 DEBUG --- [nio-8080-exec-1] HealthCheckController : ← Exiting healthCheck() - Result: UP
2026-02-14 22:30:15.129  INFO --- [nio-8080-exec-1] LoggingInterceptor : └─ [a1b2c3d4] GET /api/health - Status: 200 ✓ - Duration: 6ms
```

### Failed Request (Validation Error)
```
2026-02-14 22:31:00.456  INFO --- [nio-8080-exec-2] LoggingInterceptor : ┌─ [b2c3d4e5] POST /api/users - Client: 192.168.1.100
2026-02-14 22:31:00.458 DEBUG --- [nio-8080-exec-2] UserController : → Entering create(...)
2026-02-14 22:31:00.460  WARN --- [nio-8080-exec-2] LogHelper : ⚠ Validation failed - Field: email, Message: must be a valid email
2026-02-14 22:31:00.462  WARN --- [nio-8080-exec-2] LoggingInterceptor : └─ [b2c3d4e5] POST /api/users - Status: 400 ⚠ - Duration: 6ms
```

### Server Error
```
2026-02-14 22:32:00.789  INFO --- [nio-8080-exec-3] LoggingInterceptor : ┌─ [c3d4e5f6] GET /api/establishments/999 - Client: 192.168.1.100
2026-02-14 22:32:00.791 DEBUG --- [nio-8080-exec-3] EstablishmentController : → Entering findById(999)
2026-02-14 22:32:00.795 ERROR --- [nio-8080-exec-3] LogHelper : ✗ Error in findById - Context: id=999
java.lang.IllegalArgumentException: Establishment not found with id: 999
    at com.filae.api.domain.service.EstablishmentService.findById(EstablishmentService.java:45)
    ...
2026-02-14 22:32:00.798 ERROR --- [nio-8080-exec-3] LoggingInterceptor : └─ [c3d4e5f6] GET /api/establishments/999 - Status: 500 ✗ - Duration: 9ms
```

### Authentication Event
```
2026-02-14 22:33:00.123  INFO --- [nio-8080-exec-4] LoggingInterceptor : ┌─ [d4e5f6g7] POST /api/auth/login - Client: 192.168.1.100
2026-02-14 22:33:00.125  INFO --- [nio-8080-exec-4] LogHelper : 🔐 Security: Login success - User: john@example.com
2026-02-14 22:33:00.127  INFO --- [nio-8080-exec-4] LoggingInterceptor : └─ [d4e5f6g7] POST /api/auth/login - Status: 200 ✓ - Duration: 4ms
```

---

## Benefits

✅ **Full Visibility** - See every request and response  
✅ **Performance Tracking** - Identify slow endpoints  
✅ **Error Debugging** - Complete context for failures  
✅ **Security Auditing** - Track authentication events  
✅ **Request Correlation** - Follow a request through the system with UUID  
✅ **Production Ready** - File rotation, appropriate levels  

---

## Tips

1. **Use appropriate log levels** - Don't log sensitive data at INFO level
2. **Include context** - Always add relevant IDs and parameters
3. **Be consistent** - Use LogHelper methods for uniform format
4. **Monitor file size** - Log files rotate automatically
5. **Search by Request ID** - Use the UUID to trace a full request

---

**You now have complete visibility into your API! 🎉**

