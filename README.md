# 📱 Filae - Virtual Queue System API

> **Give time back to the people** - A virtual queue management system that eliminates physical waiting lines

A comprehensive REST API built with Spring Boot that enables users to remotely join establishment queues, track their position in real-time, and receive notifications when it's their turn to be served.

---

## 🎯 Overview

**Filae** is a complete virtual queue ecosystem designed to solve the problem of time lost in physical waiting lines at banks, clinics, government offices, and other service establishments. This repository contains the **Backend API**.

### The Problem
Physical waiting lines represent a massive loss of human productivity. People are forced to remain stationary for extended periods, unable to use that time for other productive activities.

### The Solution
Filae allows users to:
- Join queues remotely from their smartphones
- Track their position in real-time
- Receive notifications when it's their turn
- Only arrive at the physical location when about to be served

### System Components
- **REST API** (This repository - Spring Boot + Java 21)
- **Mobile Application** (React Native - separate repository)
- **Real-Time Notification System** (WebSocket)

### Key Features
- 🔍 **Establishment Discovery**: Search and browse available venues
- 🎫 **Remote Queue Entry**: Join queues from anywhere
- 📊 **Real-time Position Tracking**: Live updates on queue position
- 🔔 **Smart Notifications**: Get notified when it's your turn
- 🛠️ **Merchant Dashboard**: Queue management tools for businesses
- 🔐 **Secure Authentication**: JWT-based authentication system

---


## 🛠️ Tech Stack

- **Java 25** (compatible with JDK 21+)
- **Spring Boot 3.4.1**
- **PostgreSQL 18**
- **Spring Security + JWT** for authentication
- **Flyway** for database migrations
- **Lombok** for code generation
- **MapStruct** for object mapping
- **Swagger/OpenAPI** for API documentation
- **Maven** for build management

---

## 🚀 Getting Started

### Prerequisites

- JDK 21 or later (JDK 25 recommended)
- PostgreSQL 14+ running on `localhost:5432`
- Maven 3.6+ (or use included Maven Wrapper)

### Quick Setup

1. **Clone the repository**
```bash
git clone https://github.com/ViniItoi/filae-api.git
cd filae-api
```

2. **Create database**
```sql
psql -U postgres
CREATE DATABASE filae_db;
```

3. **Configure database** (if needed)

Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/filae_db
    username: postgres
    password: we_love_security_s2  # Change this!
```

4. **Run the application**
```bash
# Using Maven
mvn spring-boot:run

# Or using Maven Wrapper
./mvnw spring-boot:run     # Linux/Mac
.\mvnw.cmd spring-boot:run  # Windows
```

5. **Verify it's running**
```bash
curl http://localhost:8080/api/health
```

The API will be available at `http://localhost:8080/api`

---

## 📚 Database Schema

The application automatically creates the following tables via Flyway migrations:

| Table | Description |
|-------|-------------|
| `users` | Customer and merchant accounts |
| `establishments` | Businesses (restaurants, clinics, etc.) |
| `queues` | Active queue entries |
| `favorites` | User's favorite establishments |
| `notifications` | Queue status notifications |
| `opening_hours` | Business operating schedules |

---

## 🔌 API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Authentication (Public)
```
POST   /api/auth/register       # Register new user
POST   /api/auth/login          # Login and get JWT token
```

### Health Check (Public)
```
GET    /api/health              # Check API status (no authentication required)
```

**Note:** The `/health` endpoint is publicly accessible and does not require authentication.

### Protected Endpoints (Require JWT)

**Users**
```
GET    /api/users/{id}          # Get user profile
PUT    /api/users/{id}          # Update profile
DELETE /api/users/{id}          # Delete account
```

**Establishments**
```
GET    /api/establishments                    # List all
GET    /api/establishments/{id}               # Get details
GET    /api/establishments?category={cat}     # Filter by category
GET    /api/establishments?city={city}        # Filter by city
POST   /api/establishments                    # Create (Merchant only)
PUT    /api/establishments/{id}               # Update (Merchant only)
DELETE /api/establishments/{id}               # Delete (Merchant only)
```

**Queues**
```
POST   /api/queues/join                       # Join a queue
GET    /api/queues/my-queues                  # Get my queue entries
GET    /api/queues/{id}                       # Get queue details
GET    /api/queues/establishment/{id}         # Get establishment queue
PUT    /api/queues/{id}/cancel                # Cancel queue entry
PUT    /api/queues/{id}/call                  # Call next (Merchant)
PUT    /api/queues/{id}/finish                # Finish entry (Merchant)
```

**Favorites**
```
POST   /api/favorites                         # Add to favorites
GET    /api/favorites                         # Get my favorites
DELETE /api/favorites/{id}                    # Remove from favorites
```

**Notifications**
```
GET    /api/notifications                     # Get notifications
GET    /api/notifications?unread=true         # Get unread only
PUT    /api/notifications/{id}/read           # Mark as read
PUT    /api/notifications/read-all            # Mark all as read
```

**Opening Hours**
```
GET    /api/opening-hours/establishment/{id}  # Get hours
POST   /api/opening-hours                     # Set hours (Merchant)
PUT    /api/opening-hours/{id}                # Update hours (Merchant)
```

### API Documentation

Interactive API documentation available at:
```
http://localhost:8080/api/swagger-ui.html
```

---

## 🧪 Testing the API

### Option 1: Using Postman

Import the Postman collection located at:
```
docs/Filae_API_Postman_Collection.json
```

**Steps:**
1. Open Postman
2. Click **Import**
3. Select the JSON file
4. All 30+ endpoints will be available with examples
5. JWT token is auto-saved after login

### Option 2: Using cURL

**1. Register a user**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123!",
    "userType": "CUSTOMER"
  }'
```

**2. Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123!"
  }'
```

Save the `token` from the response.

**3. Use the token**
```bash
curl http://localhost:8080/api/establishments \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

---


## 🎓 Academic Project Information

This project was developed as part of a Mobile Development course, fulfilling the following requirements:

### Requirements Met

✅ **Backend API Implementation** - Complete REST API with authentication  
✅ **Multiple Features** - 30+ endpoints across 8 modules  
✅ **External Service Integration** - PostgreSQL database  
✅ **User Authentication** - JWT-based login system  
✅ **Backlog Documentation** - See `docs/BACKLOG.md`  
✅ **Sprint Planning** - See `docs/SPRINT_PLANNING.md` (2 sprints)  
✅ **Agile Methodology** - Feature prioritization by business value  

### Documentation

- **[BACKLOG.md](docs/BACKLOG.md)** - Complete feature backlog with user stories
- **[SPRINT_PLANNING.md](docs/SPRINT_PLANNING.md)** - Sprint 1 & 2 planning
- **[IMPLEMENTATION_GUIDE.md](docs/IMPLEMENTATION_GUIDE.md)** - Technical implementation details
- **Postman Collection** - `docs/Filae_API_Postman_Collection.json`

---

## 📁 Project Structure

```
filae-api/
├── src/
│   ├── main/
│   │   ├── java/com/filae/api/
│   │   │   ├── application/          # Controllers & DTOs
│   │   │   ├── domain/               # Entities, Repositories, Services
│   │   │   ├── infrastructure/       # Security, Config
│   │   │   └── FilaeApiApplication.java
│   │   └── resources/
│   │       ├── application.yml       # Configuration
│   │       └── db/migration/         # Flyway SQL scripts
│   └── test/                         # Test files
├── docs/
│   ├── BACKLOG.md                    # Product backlog
│   ├── SPRINT_PLANNING.md            # Sprint plans
│   ├── IMPLEMENTATION_GUIDE.md       # Technical guide
│   └── Filae_API_Postman_Collection.json
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

---

## 🔒 Security

The API uses JWT (JSON Web Token) for authentication:

1. **Register** or **Login** to receive a JWT token
2. Include the token in the `Authorization` header for protected endpoints:
```
Authorization: Bearer YOUR_JWT_TOKEN
```

**Token Configuration:**
- **Secret Key**: Configured in `application.yml` (change in production!)
- **Expiration**: 24 hours
- **Algorithm**: HS256

---

## 🐛 Troubleshooting

### Application won't start

**Check if PostgreSQL is running:**
```bash
# Windows
Get-Service postgresql*

# Linux/Mac
sudo systemctl status postgresql
```

**Verify database exists:**
```bash
psql -U postgres -l | grep filae_db
```

### Port 8080 already in use

**Find what's using the port:**
```bash
# Windows
netstat -ano | findstr :8080

# Linux/Mac
lsof -i :8080
```

**Kill the process or change the port in `application.yml`:**
```yaml
server:
  port: 8081  # Change to any available port
```

### Database connection refused

**Check credentials in `application.yml`:**
```yaml
spring:
  datasource:
    username: postgres
    password: we_love_security_s2  # Must match your PostgreSQL password
```

### Build fails

**Clean and rebuild:**
```bash
mvn clean install -U
```

---

## 🤝 Contributing

This is an academic project, but contributions are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Vinicius Itoi**
- GitHub: [@ViniItoi](https://github.com/ViniItoi)

---

## 🙏 Acknowledgments

- Spring Boot community for excellent documentation
- FIAP for the academic challenge
- PostgreSQL team for the robust database

---

**⭐ If you find this project useful, please consider giving it a star!**
