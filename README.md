# Conflict Tracker API

A REST API for tracking global conflicts, built with Spring Boot as part of the Backend Programming module.

## 📋 Description

Conflict Tracker is a comprehensive API system for managing and querying information about global conflicts. It allows tracking of:

- **Conflicts**: Armed conflicts with their status, dates, and involved countries
- **Factions**: Groups/parties participating in each conflict
- **Countries**: Nations involved in or supporting factions in conflicts
- **Events**: Key historical events associated with each conflict

## 🛠️ Technologies

- **Java 17**
- **Spring Boot 3.5.8**
- **Spring Data JPA** (Hibernate)
- **H2 Database** (Development)
- **PostgreSQL** (Production-ready)
- **Gradle** (Build tool)

## 📁 Project Structure

```
src/
├── main/
│   ├── java/ConflictTracker/conflict_tracker/
│   │   ├── config/           # Exception handlers, configuration
│   │   ├── controller/       # REST controllers
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── model/            # JPA entities
│   │   ├── repository/       # Spring Data repositories
│   │   └── service/          # Business logic layer
│   └── resources/
│       ├── static/           # Frontend files (index.html)
│       ├── application.yaml  # Application configuration
│       └── data.sql          # Initial data
└── test/                     # Test files
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Gradle (or use the included wrapper)

### Running the Application

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/conflict-tracker.git
   cd conflict-tracker
   ```

2. **Run with Gradle**
   ```bash
   # On Windows
   .\gradlew.bat bootRun

   # On Linux/Mac
   ./gradlew bootRun
   ```

3. **Access the application**
   - API: http://localhost:8080/api/v1
   - Frontend: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:conflictdb`)

### Building the Application

```bash
# Build the project
.\gradlew.bat build

# Run tests
.\gradlew.bat test
```

## 📡 API Endpoints

### Conflicts

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/conflicts` | Get all conflicts |
| GET | `/api/v1/conflicts?status=ACTIVE` | Filter conflicts by status |
| GET | `/api/v1/conflicts/{id}` | Get conflict details by ID |
| POST | `/api/v1/conflicts` | Create a new conflict |
| PUT | `/api/v1/conflicts/{id}` | Update a conflict |
| DELETE | `/api/v1/conflicts/{id}` | Delete a conflict |

### Countries

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/countries` | Get all countries |
| GET | `/api/v1/countries/{id}` | Get country by ID |
| GET | `/api/v1/countries/{code}/conflicts` | Get conflicts involving a country |
| POST | `/api/v1/countries` | Create a new country |
| PUT | `/api/v1/countries/{id}` | Update a country |
| DELETE | `/api/v1/countries/{id}` | Delete a country |

### Factions

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/factions` | Get all factions |
| GET | `/api/v1/factions?conflictId={id}` | Get factions by conflict |
| GET | `/api/v1/factions/{id}` | Get faction by ID |
| POST | `/api/v1/factions` | Create a new faction |
| PUT | `/api/v1/factions/{id}` | Update a faction |
| DELETE | `/api/v1/factions/{id}` | Delete a faction |

### Events

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/events` | Get all events |
| GET | `/api/v1/events?conflictId={id}` | Get events by conflict |
| GET | `/api/v1/events/{id}` | Get event by ID |
| POST | `/api/v1/events` | Create a new event |
| PUT | `/api/v1/events/{id}` | Update an event |
| DELETE | `/api/v1/events/{id}` | Delete an event |

## 📝 Example Usage with curl

### Get all conflicts
```bash
curl -X GET http://localhost:8080/api/v1/conflicts
```

### Get active conflicts only
```bash
curl -X GET "http://localhost:8080/api/v1/conflicts?status=ACTIVE"
```

### Get conflict details by ID
```bash
curl -X GET http://localhost:8080/api/v1/conflicts/1
```

### Create a new conflict
```bash
curl -X POST http://localhost:8080/api/v1/conflicts \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Conflict",
    "startDate": "2024-01-01",
    "status": "ACTIVE",
    "description": "Description of the conflict",
    "countryCodes": ["USA", "RUS"]
  }'
```

### Update a conflict
```bash
curl -X PUT http://localhost:8080/api/v1/conflicts/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Conflict Name",
    "startDate": "2022-02-24",
    "status": "FROZEN",
    "description": "Updated description",
    "countryCodes": ["UKR", "RUS"]
  }'
```

### Delete a conflict
```bash
curl -X DELETE http://localhost:8080/api/v1/conflicts/1
```

### Get conflicts by country code
```bash
curl -X GET http://localhost:8080/api/v1/countries/UKR/conflicts
```

### Create a new faction
```bash
curl -X POST http://localhost:8080/api/v1/factions \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Faction",
    "conflictId": 1,
    "supportingCountryCodes": ["USA"]
  }'
```

### Create a new event
```bash
curl -X POST http://localhost:8080/api/v1/events \
  -H "Content-Type: application/json" \
  -d '{
    "eventDate": "2024-01-15",
    "location": "City Name",
    "description": "Description of the event",
    "conflictId": 1
  }'
```

## 🗃️ Data Model

### Entity Relationships

```
Conflict (1) -----> (N) Faction
    |                     |
    |                     |
    v                     v
(M:N) Country <---- (M:N) Country (supporting)
    
Conflict (1) -----> (N) Event
```

### Status Values

- `ACTIVE`: Ongoing conflict
- `FROZEN`: Conflict is paused but not resolved
- `ENDED`: Conflict has concluded

## ⚙️ Configuration

### Development (H2 - default)

The application uses H2 in-memory database by default. Configuration is in `application.yaml`.

### Production (PostgreSQL)

To use PostgreSQL, update `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/conflictdb
    driver-class-name: org.postgresql.Driver
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

## 👨‍💻 Author

Conflict Tracker - Backend Programming Project

## 📄 License

This project is for educational purposes.

