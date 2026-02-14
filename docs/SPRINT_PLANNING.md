# Sprint Planning - Filae

## Sprint 1 (2 weeks) - ⚠️ INCOMPLETE

**Goal:** Implement core queue functionality with user authentication  
**Actual Result:** Infrastructure completed, but no feature implementation

### High Priority Stories (Must Have)
- [ ] User login/authentication - **NOT STARTED**
- [ ] List available establishments - **NOT STARTED**
- [ ] Join queue functionality - **NOT STARTED**

### Completed ✅
- [x] Project setup (Maven, Spring Boot, PostgreSQL)
- [x] Database schema design (6 tables via Flyway migrations)
- [x] Domain entities (User, Establishment, Queue)
- [x] Repository layer (3 repositories)
- [x] Security infrastructure (Spring Security + JWT config)
- [x] Logging infrastructure (Request/Response logging)
- [x] Exception handling (Global exception handler)
- [x] API documentation setup (Swagger/OpenAPI)

### Sprint Metrics
- Story Points Planned: 21
- Story Points Completed: 8 (Infrastructure only)
- **Features Delivered: 0/3**

### Sprint Review Notes
**Achievements:**
- Solid technical foundation established
- All infrastructure components in place
- Database schema complete and tested

**Gaps:**
- No service layer implemented
- No business logic
- No API endpoints (except health check)

---

## Sprint 2 (2 weeks) - PLANNED

**Goal:** Complete Sprint 1 features + Merchant operations

### Critical Priority (Sprint 1 Carry-over)
- [ ] **AUTH-001:** Implement user authentication service
  - [ ] Create AuthService with registration logic
  - [ ] Create AuthController with /register and /login endpoints
  - [ ] Implement JwtTokenProvider for token generation
  - [ ] Add password hashing (BCrypt)
  - Story Points: 8

- [ ] **EST-001:** Implement establishment listing
  - [ ] Create EstablishmentService with CRUD operations
  - [ ] Create EstablishmentController with REST endpoints
  - [ ] Implement DTOs (EstablishmentDTO, CreateEstablishmentRequest)
  - Story Points: 5

- [ ] **QUEUE-001:** Implement queue join functionality
  - [ ] Create QueueService with join logic
  - [ ] Create TicketService for ticket generation
  - [ ] Create QueueController with /join endpoint
  - [ ] Implement position calculation algorithm
  - Story Points: 8

### Medium Priority
- [ ] **QUEUE-002:** Implement queue cancellation
  - [ ] Add cancel endpoint to QueueController
  - [ ] Update state machine transitions
  - Story Points: 3

- [ ] **MERCHANT-001:** Implement "Call Next" functionality
  - [ ] Create MerchantService
  - [ ] Add /queues/next endpoint
  - [ ] Trigger notifications on state change
  - Story Points: 5

### Low Priority
- [ ] **NOTIF-001:** Basic notification system
  - [ ] Create NotificationService
  - [ ] Store notifications in database
  - Story Points: 3

### Sprint 2 Metrics
- Story Points Planned: 32
- Sprint Velocity Target: 25
- Risk: High (carry-over from Sprint 1)

### Definition of Done
- [ ] All endpoints tested via Postman
- [ ] Integration tests written
- [ ] Postman collection updated
- [ ] README updated with new endpoints
- [ ] Code reviewed and documented

---

## Backlog (Future Sprints)

### Sprint 3 Candidates
- Real-time position updates (WebSocket)
- Favorites functionality
- Opening hours management
- Merchant dashboard API
- Search and filtering

### Technical Debt
- [ ] Add comprehensive unit tests
- [ ] Implement caching strategy
- [ ] Add API rate limiting
- [ ] Performance optimization
- [ ] Security hardening (OWASP compliance)
