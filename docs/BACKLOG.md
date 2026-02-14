# 📋 Project Backlog: Filae Ecosystem

This backlog outlines the functional and technical requirements for the Filae virtual queuing system, organized by Epics to ensure a scalable and structured development process.

---

## 🏛️ Epic 1: System Foundation & Architecture ✅ COMPLETE
*Focus: Establishing the core infrastructure and high-level architectural patterns.*

- [x] **Technical:** Initialize Spring Boot API with Java 21 and PostgreSQL integration.
- [x] **Technical:** Configure MapStruct and Lombok for clean DTO-to-Entity mapping.
- [x] **Technical:** Implement Database Migration strategy (Flyway) for versioned schema control.
- [ ] **Technical:** Setup Mobile Repository (React Native/Flutter) with clean architecture and navigation.
- [x] **Documentation:** Configure Swagger/OpenAPI for automated API endpoint documentation.
- [x] **Technical:** Implement logging infrastructure (Request/Response logging)
- [x] **Technical:** Setup Spring Security with JWT configuration
- [x] **Technical:** Implement global exception handling

## 🔍 Epic 2: Establishment Discovery
*Focus: Enabling users to locate and view queuing venues.*

- [ ] **User Story:** As a user, I want to list available establishments so I can choose where to join a queue.
- [ ] **User Story:** As a user, I want to view specific details (name, capacity, current wait time) of an establishment.
- [ ] **Technical:** Develop `Establishment` CRUD and optimized lookup endpoints.
- [ ] **UI:** Build the "Discovery" screen with search and category filtering in the mobile app.

## 🔄 Epic 3: Core Queue Logic
*Focus: The engine of the application—managing entries and position sequences.*

- [ ] **User Story:** As a user, I want to join a queue remotely to secure my spot without being physically present.
- [ ] **Technical:** Implement `QueueEntry` service logic (unique ticket generation and position calculation).
- [ ] **User Story:** As a user, I want to cancel my ticket if I no longer require the service.
- [ ] **Technical:** Define State Machine for Queue Status: `WAITING` ➔ `CALLED` ➔ `FINISHED` (or `CANCELLED`).



## 📱 Epic 4: Real-Time Experience
*Focus: Synchronizing the user's mobile state with the backend queue status.*

- [ ] **User Story:** As a user, I want to see my position update in real-time as the queue advances.
- [ ] **Technical:** Implement a synchronization strategy (Long Polling or WebSockets) for position decrements.
- [ ] **UI:** Design and implement the "Live Ticket" screen with dynamic position display and estimated wait time.

## 🛠️ Epic 5: Merchant Operations
*Focus: Providing tools for the business owners to manage the flow.*

- [ ] **User Story:** As a merchant, I want to "Call Next" to advance the queue and notify the next user.
- [ ] **Technical:** Create the `PATCH /queues/next` endpoint to update the queue state and trigger notifications.
- [ ] **User Story:** As a merchant, I want to view a dashboard of current waiting users.

---
*Note: This backlog is subject to refinement as the MVP evolves.*