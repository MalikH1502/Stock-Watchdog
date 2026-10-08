# StockWatchdog

StockWatchdog is a Spring Boot web application for searching stocks, viewing cached quotes, and creating browser-evaluated price alerts.

**Live demo:** _Add the deployed HTTPS URL here once the hosted environment is configured._

## Features

- Search Alpha Vantage symbols and cache discovered stocks in PostgreSQL.
- Refresh a stock quote when its cached price is older than five minutes.
- Create and manage alerts for prices above or below a target.
- Session-based login with BCrypt password hashing and Spring Security CSRF protection.
- Redis-backed HTTP sessions.

## Screenshots

Add current screenshots of the dashboard, alerts page, and login flow here before sharing this project publicly.

## Tech stack

- Java 17+
- Spring Boot 4
- Spring Security and Spring Session Data Redis
- Spring Data JPA with PostgreSQL
- Alpha Vantage market data
- Maven Wrapper

## Running locally

### Prerequisites

- Java 17 or newer
- PostgreSQL
- Redis
- An Alpha Vantage API key

Set these environment variables before starting the application:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/stockwatchdog
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your-password
ALPHA_VANTAGE_API_KEY=your-key
```

For a local HTTP session cookie, `SESSION_COOKIE_SECURE` can remain unset. Set it to `true` whenever the application is served over HTTPS:

```text
SESSION_COOKIE_SECURE=true
```

Start the application with the Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

Open `http://localhost:8080/signup.html` to create an account.

## API summary

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/csrf` | Get the CSRF token used by browser forms |
| `POST` | `/api/signup` | Register a user and redirect to the login page |
| `POST` | `/api/login` | Authenticate a user and create a session |
| `POST` | `/api/logout` | End the current session |
| `GET` | `/api/stocks/search?query=...` | Search Alpha Vantage symbols |
| `GET` | `/api/stocks/{id}` | View a stock and refresh its cached quote when stale |
| `GET` | `/api/alerts` | List alerts for the authenticated user |
| `POST` | `/api/alerts` | Create an alert |
| `DELETE` | `/api/alerts/{id}` | Delete an alert |
| `GET` | `/api/help` | List help articles |

## Data and deployment requirements

The deployed service requires hosted PostgreSQL and Redis. Spring Session stores sessions in Redis, so the application will not be fully functional without both services. The deployment host must provide the environment variables above; secrets should not be committed to `application.properties`.

For HTTPS deployments, configure TLS at the host or reverse proxy and set `SESSION_COOKIE_SECURE=true`. SQL logging is disabled by default; set `SPRING_JPA_SHOW_SQL=true` only while diagnosing a local issue.

## Deploying to Render with Docker

Create a Render **Web Service** connected to this repository and select **Docker** as the runtime. Render will build the root `Dockerfile` and provide the `PORT` environment variable automatically.

Provision a Render PostgreSQL database and a Render Key Value (Redis) instance, then add these environment variables to the web service:

```text
SPRING_DATASOURCE_URL=<Render PostgreSQL internal JDBC URL>
SPRING_DATASOURCE_USERNAME=<Render PostgreSQL username>
SPRING_DATASOURCE_PASSWORD=<Render PostgreSQL password>
REDIS_HOST=<Render Key Value internal hostname>
REDIS_PORT=<Render Key Value internal port>
REDIS_PASSWORD=<Render Key Value password, if enabled>
ALPHA_VANTAGE_API_KEY=<your Alpha Vantage key>
SESSION_COOKIE_SECURE=true
```

Use Render's **internal** database and Redis connection values when all services are in the same Render account and region. Do not commit these values to the repository. Configure the web service health check as `/actuator/health` after confirming that endpoint is reachable in the deployed security configuration.

## Known limitations

- Alpha Vantage's free tier allows 25 requests per day shared across searches and quotes. When the limit is reached, searches show a clear error and stock detail pages continue serving the last cached quote.
- Alerts are evaluated client-side while an authenticated StockWatch page is open; they are not evaluated while the site is closed.
- Watchlist persistence exists in the API, but the watchlist page is unfinished and is intentionally not linked from the application navigation.
- Quote freshness depends on the external Alpha Vantage service and the last successful refresh.
