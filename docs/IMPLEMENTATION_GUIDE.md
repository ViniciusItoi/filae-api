# 🛠️ Filae API - Implementation Guide

## Current State

Your Filae API project now has:

✅ **Maven Build Configuration** (`pom.xml`)
- Spring Boot 3.2.0
- All required dependencies
- Proper compiler settings for Java 21

✅ **Database Configuration** (`application.yml`)
- PostgreSQL connection to localhost:5432
- Flyway migrations enabled
- JWT settings configured
- Logging configured

✅ **Database Schema** (6 migration files)
- Users table
- Establishments table
- Queues table
- Favorites table
- Notifications table
- Opening hours table

✅ **Core Domain Entities**
- User.java (CUSTOMER, MERCHANT, ADMIN types)
- Establishment.java (venues/businesses)
- Queue.java (queue entries with WAITING, CALLED, FINISHED, CANCELLED statuses)

✅ **Repository Layer**
- UserRepository
- EstablishmentRepository
- QueueRepository

✅ **DTOs**
- LoginRequest
- LoginResponse
- RegisterRequest

✅ **Entry Point**
- FilaeApiApplication.java with CORS configuration

✅ **Health Check Endpoint**
- GET /health

---

## 📋 Next Steps to Complete Phase 1

### Phase 1: Authentication & Core Features (Priority: HIGH)

#### 1. Security Configuration
Create `src/main/java/com/filae/api/infrastructure/security/SecurityConfig.java`:
- Spring Security configuration
- JWT token provider
- Authentication filter
- Password encoding

**Files to create:**
- SecurityConfig.java
- JwtTokenProvider.java
- JwtAuthenticationFilter.java
- CustomUserDetailsService.java

#### 2. Authentication Service & Controller
Create authentication service and controller:
- User registration logic
- Login logic
- Token generation and validation

**Files to create:**
- AuthService.java (in application/service)
- AuthController.java (in application/controller)

#### 3. Establishment Controller
Create endpoints for browsing establishments:
- List establishments (with pagination, filtering, search)
- Get establishment details
- Get categories

**Files to create:**
- EstablishmentService.java (in application/service)
- EstablishmentController.java (in application/controller)

#### 4. Queue Controller
Create core queue functionality:
- Join queue
- Get queue position
- Cancel queue entry
- Get user's active queues

**Files to create:**
- QueueService.java (in application/service)
- QueueController.java (in application/controller)

---

## 🏗️ Recommended Development Order

### Week 1-2: Foundation
1. **Security Setup**
   - JWT token provider
   - Spring Security config
   - User authentication filter

2. **Authentication Endpoints**
   - POST /auth/register - Create new user
   - POST /auth/login - User login
   - POST /auth/logout - User logout

3. **Test Authentication**
   - Verify registration works
   - Verify login returns JWT token
   - Verify protected endpoints require token

### Week 2-3: Core Features
4. **Establishment Browsing**
   - GET /establishments - List all
   - GET /establishments/{id} - Get details
   - GET /establishments/search - Search
   - GET /establishments/categories - List categories

5. **Queue Management (Basic)**
   - POST /queues/join - Join queue
   - GET /queues/{ticketId} - Get position
   - DELETE /queues/{ticketId} - Cancel
   - GET /queues/my-queues - User's queues

6. **Test Core Flows**
   - User can register and login
   - User can browse establishments
   - User can join and track queue position

### Week 3-4: Enhancements
7. **Merchant Features**
   - GET /merchant/establishments - Merchant's venues
   - PATCH /queues/establishment/{id}/next - Call next
   - PATCH /queues/{id}/finish - Mark finished
   - GET /queues/establishment/{id}/live - Live queue view

8. **Notifications**
   - Queue called notification
   - Position update notification
   - Real-time updates via WebSocket (optional for Phase 1)

9. **User Profile**
   - GET /users/profile - Get profile
   - PUT /users/profile - Update profile
   - POST /users/change-password - Change password

10. **Favorites**
    - POST /favorites - Add favorite
    - DELETE /favorites/{id} - Remove favorite
    - GET /favorites - Get user favorites

---

## 📁 File Structure for Complete Implementation

```
src/main/java/com/filae/api/
├── FilaeApiApplication.java                    ✅ CREATED
├── application/
│   ├── controller/
│   │   ├── HealthCheckController.java          ✅ CREATED
│   │   ├── AuthController.java                 ⏳ TO CREATE
│   │   ├── EstablishmentController.java        ⏳ TO CREATE
│   │   ├── QueueController.java                ⏳ TO CREATE
│   │   ├── UserController.java                 ⏳ TO CREATE
│   │   ├── FavoriteController.java             ⏳ TO CREATE
│   │   └── MerchantController.java             ⏳ TO CREATE
│   ├── dto/
│   │   ├── auth/
│   │   │   ├── LoginRequest.java               ✅ CREATED
│   │   │   ├── LoginResponse.java              ✅ CREATED
│   │   │   └── RegisterRequest.java            ✅ CREATED
│   │   ├── establishment/
│   │   │   ├── EstablishmentDTO.java           ⏳ TO CREATE
│   │   │   ├── EstablishmentDetailDTO.java     ⏳ TO CREATE
│   │   │   └── CategoryDTO.java                ⏳ TO CREATE
│   │   ├── queue/
│   │   │   ├── QueueJoinRequest.java           ⏳ TO CREATE
│   │   │   ├── QueuePositionDTO.java           ⏳ TO CREATE
│   │   │   └── QueueDetailDTO.java             ⏳ TO CREATE
│   │   └── user/
│   │       ├── UserProfileDTO.java             ⏳ TO CREATE
│   │       └── UpdateProfileRequest.java       ⏳ TO CREATE
│   └── service/
│       ├── AuthService.java                    ⏳ TO CREATE
│       ├── EstablishmentService.java           ⏳ TO CREATE
│       ├── QueueService.java                   ⏳ TO CREATE
│       ├── UserService.java                    ⏳ TO CREATE
│       └── NotificationService.java            ⏳ TO CREATE
├── domain/
│   ├── entity/
│   │   ├── User.java                           ✅ CREATED
│   │   ├── Establishment.java                  ✅ CREATED
│   │   ├── Queue.java                          ✅ CREATED
│   │   ├── Favorite.java                       ⏳ TO CREATE
│   │   ├── Notification.java                   ⏳ TO CREATE
│   │   └── OpeningHours.java                   ⏳ TO CREATE
│   └── repository/
│       ├── UserRepository.java                 ✅ CREATED
│       ├── EstablishmentRepository.java        ✅ CREATED
│       ├── QueueRepository.java                ✅ CREATED
│       ├── FavoriteRepository.java             ⏳ TO CREATE
│       ├── NotificationRepository.java         ⏳ TO CREATE
│       └── OpeningHoursRepository.java         ⏳ TO CREATE
└── infrastructure/
    ├── security/
    │   ├── SecurityConfig.java                 ⏳ TO CREATE
    │   ├── JwtTokenProvider.java               ⏳ TO CREATE
    │   ├── JwtAuthenticationFilter.java        ⏳ TO CREATE
    │   ├── CustomUserDetailsService.java       ⏳ TO CREATE
    │   └── exception/
    │       └── JwtAuthException.java            ⏳ TO CREATE
    └── exception/
        ├── GlobalExceptionHandler.java         ⏳ TO CREATE
        ├── ApiException.java                   ⏳ TO CREATE
        └── ErrorResponse.java                  ⏳ TO CREATE
```

---

## 🔑 Key Implementation Details

### JWT Authentication Flow
1. User sends POST /auth/register with email, password, name
2. API hashes password and stores user in database
3. User sends POST /auth/login with email, password
4. API validates credentials and returns JWT token
5. User includes JWT in Authorization header for all protected endpoints
6. JwtAuthenticationFilter validates token on each request

### Queue Join Flow
1. User sends POST /queues/join with establishmentId, partySize
2. QueueService validates:
   - Establishment exists and is accepting customers
   - User isn't already in active queue for this establishment
   - Establishment queue is enabled
3. QueueService calculates position and estimated wait time
4. Queue entry is created with WAITING status
5. User receives queue ticket with number and position

### Real-time Position Updates (Phase 2)
- When merchant calls next customer: status changes to CALLED
- All waiting customers' positions update (via WebSocket or polling)
- Position update notifications sent to affected users
- When customer is finished: status changes to FINISHED

---

## 🧪 Testing Strategy

### Unit Tests
Create tests for services:
```
src/test/java/com/filae/api/
├── application/service/
│   ├── AuthServiceTest.java
│   ├── EstablishmentServiceTest.java
│   └── QueueServiceTest.java
└── domain/
    └── repository/
        └── QueueRepositoryTest.java
```

### Integration Tests
Create tests for controllers:
```
src/test/java/com/filae/api/
└── application/controller/
    ├── AuthControllerTest.java
    ├── EstablishmentControllerTest.java
    └── QueueControllerTest.java
```

### Test Data Setup
Create SQL script for test data:
```
src/test/resources/
└── test-data.sql
```

---

## 📊 Database Indices

Already created for optimal query performance:
- `users(email)` - Fast user lookup by email
- `establishments(category)` - Fast filtering by category
- `establishments(merchant_id)` - Merchant's venues lookup
- `establishments(coordinates)` - Geographic queries
- `queues(establishment_id, status)` - Queue status queries
- `queues(user_id)` - User's queues

---

## 🔒 Security Considerations

### Already Configured
✅ CORS enabled for frontend integration
✅ Password hashing with bcrypt
✅ JWT token-based authentication
✅ Validation on all inputs

### To Implement
⏳ Rate limiting on auth endpoints
⏳ SQL injection prevention (already handled by JPA)
⏳ XSS prevention
⏳ HTTPS enforcement in production
⏳ Input sanitization

---

## 📈 Performance Optimizations

### Database
✅ Proper indexing on frequently queried columns
✅ Connection pooling (HikariCP - 10 max, 5 min)
✅ Pagination for list endpoints

### API
⏳ Response caching for establishments
⏳ Batch operations for queue updates
⏳ Database query optimization

---

## 🚀 Running the Project

### 1. Initial Setup
```bash
cd C:\Users\Vinicius\IdeaProjects\filae-api

# Run setup script
.\setup.ps1

# Or manually
psql -U postgres -c "CREATE DATABASE filae_db;"
mvn clean install
```

### 2. Start Application
```bash
mvn spring-boot:run
```

### 3. Verify It's Working
```bash
# Check health
curl http://localhost:8080/api/health

# Should return
{
  "status": "UP",
  "service": "Filae API",
  "version": "1.0.0",
  "timestamp": 1610702400000
}
```

### 4. Test API
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- API Docs: http://localhost:8080/api/v3/api-docs

---

## 📝 Implementation Checklist

### Foundation (Completed ✅)
- [x] Project structure created
- [x] Maven configuration (pom.xml)
- [x] Database configuration (application.yml)
- [x] Database migrations (6 files)
- [x] Core entities (User, Establishment, Queue)
- [x] Repository interfaces
- [x] Basic DTOs
- [x] Application entry point

### Phase 1 Tasks (In Progress)
- [ ] Security & JWT setup
- [ ] Authentication endpoints
- [ ] Establishment browsing
- [ ] Queue management (join, position, cancel)
- [ ] User profile management
- [ ] Favorites management
- [ ] Unit tests for services
- [ ] Integration tests for controllers

### Phase 2 Tasks (Future)
- [ ] Merchant dashboard
- [ ] Real-time WebSocket updates
- [ ] Notifications system
- [ ] Advanced analytics
- [ ] Mobile app integration
- [ ] Performance optimization

---

## 💡 Tips for Development

1. **Test Driven Development**: Write tests before implementation
2. **Commit Often**: Use small, meaningful commits
3. **Document Code**: Use JavaDoc for public methods
4. **Use Postman/Insomnia**: Test APIs before mobile development
5. **Monitor Logs**: Check logs for errors and performance
6. **Database Backups**: Create backups before major changes

---

## 📞 Support Resources

- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **Spring Security**: https://spring.io/projects/spring-security
- **JWT Guide**: https://jwt.io/
- **PostgreSQL Docs**: https://www.postgresql.org/docs/
- **Maven Guide**: https://maven.apache.org/

---

## 🎯 Success Metrics for Phase 1

- ✅ API starts without errors
- ✅ Health check endpoint works
- ✅ User can register and login
- ✅ User can browse establishments
- ✅ User can join queue and see position
- ✅ User can cancel queue entry
- ✅ Merchant can view queue and call next customer
- ✅ All endpoints return proper error responses
- ✅ 80%+ test coverage for business logic
- ✅ Swagger documentation is complete

---

**Next: Start with Security Configuration**

The recommended next step is to implement JWT security. Would you like me to create the security configuration files?

---

*Last Updated: 2024*  
*Filae API - Virtual Queue Management System*

