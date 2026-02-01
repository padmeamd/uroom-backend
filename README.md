# Uroom Backend

Backend service for **Uroom** — a platform for forming small, mission-based creative and academic groups (Rooms).

This project is built with **Spring Boot** and follows a **clean layered architecture**, designed to be scalable, maintainable, and suitable for enterprise-level development and research.

---

## Project Overview

Uroom is not a dating or chat application.  
It focuses on **small, goal-oriented groups** (2–8 people) formed around a concrete mission, such as:
- short film production
- songwriting sessions
- portfolio reviews
- study or research collaboration

This repository contains the **backend API** responsible for:
- business logic
- data persistence
- validation
- security (planned)
- integration with frontend clients

---

## Architecture

The project follows a **layered / clean architecture** approach:

Controller → Service → Repository → Domain


### Package structure:

```
com.uroom.backend
├── config # CORS, security, and infrastructure configuration
├── controller # REST API endpoints
├── service # Business logic and use cases
├── repository # Data access layer
├── domain # Core domain entities
├── dto # API data transfer objects
├── mapper # Entity ↔ DTO mapping
└── exception # Centralised error handling
```
Each feature is developed **vertically**, ensuring clear separation of concerns and testability.

---

## Domain Model

The core entity is `Room`, representing a small mission-based group.

Key design decisions:
- UUID identifiers for distributed-system readiness
- Explicit domain invariants
- No setters on entities
- Clean separation between entities and API contracts

---

## Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Spring Web**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Maven**

---

## API (Early Stage)

Planned base path:


/api


Example endpoints (in progress):
```
GET /api/rooms → list all rooms
POST /api/rooms → create a new room
```

---

## Security (Planned)

- JWT-based authentication
- Role-based access control
- Stateless backend design

---

## Database

- PostgreSQL
- Schema managed via JPA (migrations planned)
- Designed to allow future replacement of Hibernate with a custom ORM implementation

---

## Getting Started

### Prerequisites
- Java 17+
- PostgreSQL
- Gradle

### Run locally
```bash
./gradlew bootRun
```

The application will start on:
```
http://localhost:8080
```

### Frontend Integration

The backend is designed to work with a separate frontend application (React / Vite).

CORS is configured to allow:

```
http://localhost:5173
```
deployed frontend domains

### Development Philosophy

This project is intentionally designed to:

follow enterprise backend practices

- remain readable and explainable

- be suitable for academic work (MSc / PhD)

support experimentation with persistence and architecture

## Roadmap

 Core project structure

 Domain entity design

 DTO & mapper layer

 Service layer

 Validation

 Authentication & authorization

 Deployment

 Performance experiments (Hibernate vs custom ORM)
 
### Author : Daria Konstantinova

Backend & full-stack developer
Research-oriented software engineering
