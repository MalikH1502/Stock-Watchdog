# 📈 StockWatchdog

A Spring Boot REST API for monitoring stocks, managing watchlists, and setting price alerts.

---

## Features

- View and manage a list of tracked stocks
- Add stocks to a personal watchlist
- Create price alerts (trigger when a stock goes above or below a target value)
- User account management
- Historical price tracking per stock

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Framework | Spring Boot |
| Persistence | Spring Data JPA |
| Database | Configurable via `application.properties` |
| Boilerplate reduction | Lombok (`@Data`, `@NoArgsConstructor`) |

---

## Project Structure

```
src/main/java/com/malikh/StockWatchdog/
│
├── Controllers/
│   └── MyController.java        # REST endpoints
│
├── Entity/
│   ├── Alert.java               # Price alert with ABOVE/BELOW condition
│   ├── History.java             # Historical price record for a stock
│   ├── Stock.java               # Stock symbol + company name
│   ├── User.java                # Application user
│   └── WatchlistEntry.java      # Many-to-many join: User <-> Stock
│
├── Repository/
│   ├── AlertRepository.java
│   ├── StockRepository.java
│   ├── UserRepository.java
│   └── WatchlistRepository.java
│
├── Service/
│   ├── AlertService.java
│   ├── StockService.java
│   ├── UserService.java
│   └── WatchlistService.java
│
└── StockWatchdogApplication.java
```

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/stocks` | Get all stocks |
| `GET` | `/api/stocks/{symbol}` | Get a specific stock by symbol |
| `POST` | `/api/stocks` | Create a new stock |
| `POST` | `/api/watchlist` | Add a stock to a user's watchlist |
| `POST` | `/api/alerts` | Create a price alert |
| `POST` | `/api/users` | Create a new user |

---

## Data Model Overview

```
User ──< WatchlistEntry >── Stock
 │                            │
 └──< Alert >────────────────┘
                   │
                History
```

- A **User** can have many **WatchlistEntries** and many **Alerts**
- A **Stock** can appear in many watchlists and have many alerts and history records
- An **Alert** fires when a stock's price goes `ABOVE` or `BELOW` a set value

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- A running database (configure in `application.properties`)

### Run

```bash
mvn spring-boot:run
```

### Configuration

Edit `src/main/resources/application.properties` to set your database connection:

```properties
spring.datasource.url=jdbc:your-db-url
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
```

---

## Known Limitations / TODO

- [ ] No authentication/authorization (passwords stored in plain text)
- [ ] No input validation on endpoints
- [ ] Alert evaluation logic not yet implemented
- [ ] History population requires an external data source integration
