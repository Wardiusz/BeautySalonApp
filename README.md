> # Beauty Salon Explorer (Warsaw)
> A full-stack application that fetches, stores, and displays hair and beauty salons sourced from the Google Places API (New).
>

---

- [How to run](#how-to-run)
- [Tech Stack & Solution](#tech-stack--solution)
- [Running the App](#running-the-app)
- [Developer Notes](#developer-notes)

## How to Run

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running
- A valid **Google Places API key** with the **Places API (New)** enabled

### 1. Clone the repository

```bash
git clone https://github.com/your-username/BeautySalonExplorerApp.git
cd BeautySalonExplorerApp
```

### 2. Configure environment variables

```bash
cp .env.example .env
```

Open `.env` and fill in your values:

```dotenv
DB_USERNAME=user
DB_PASSWORD=password
GOOGLE_PLACES_API_KEY=YOUR_GOOGLE_PLACES_KEY_HERE
```

optionally add:

```dotenv
DB_HOST=localhost
DB_PORT=8080
DB_NAME=example-db
```

### 3. Start the application

```bash
docker compose up --build
```

This starts two containers:
- **PostgreSQL** on port `5432`
- **Spring Boot API** on port `8080`

The app waits for the database to be healthy before starting.

### 4. Seed the database with salons

Run this once to fetch salons from Google Places and populate the database:

```bash
docker compose run --rm -e SPRING_PROFILES_ACTIVE=prod,seed app
```

The number of salons fetched is controlled by:

```properties
google.places.target-count=100
```

in `application.properties`.

### 5. Access the API

| Endpoint | Method | Description |
|---|---|---|
| `/api/v1/salons` | GET | List all salons |
| `/api/v1/salons?district=Mokotów` | GET | Filter by district |
| `/api/v1/salons/{id}` | GET | Get salon details |
| `/api/v1/salons/{id}/update` | PUT | Update salon info |

### Running locally without Docker (development)

```bash
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Uses H2 in-memory database. H2 console available at `http://localhost:8080/h2-console`.

To seed in dev mode:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev,seed
```

### 6. Start the frontend

Run this in a separate terminal:
```bash
cd frontend
npm install
npm run dev
```

then open `http://localhost:5173` in your browser.

---

## Tech Stack & Solution

### Architecture

```
┌─────────────────┐     HTTP      ┌──────────────────────┐     JDBC     ┌──────────────┐
│   React (Vite)  │ <──────────>  │  Spring Boot (REST)  │ <──────────> │  PostgreSQL  │
│   port 3000     │               │  port 8080           │              │  port 5432   │
└─────────────────┘               └──────────┬───────────┘              └──────────────┘
                                             │ HTTPS
                                             ▼
                                  ┌──────────────────────┐
                                  │  Google Places API   │
                                  │  (New)               │
                                  └──────────────────────┘
```

### Backend — Spring Boot

| Technology | Purpose |
|---|---|
| Spring Boot 4 | Application framework |
| Spring Data JPA + Hibernate | ORM and database access |
| Spring Web (RestTemplate) | HTTP client for Google Places API |
| PostgreSQL | Production database |
| H2 | Development / test database |
| Resilience4j | Retry and rate limiting for external API calls |
| Lombok | Boilerplate reduction (`@Data`, `@Builder`, `@Slf4j`) |
| Jackson | JSON serialization / deserialization |
| Docker + Docker Compose | Containerisation and orchestration |

**Project structure follows a layered architecture:**

```
controller/         → REST endpoints
service/            → interfaces
    impl/           → business logic
mapper/             → entity to DTO conversion
dto/                → request/response objects
    places/         → Google Places API DTOs
entity/             → JPA entities
repository/         → Spring Data repositories
config/             → RestTemplate, CORS, app config
```

### Frontend — React

| Technology | Purpose |
|---|---|
| React 18 | UI framework |
| Vite | Build tool and dev server |
| Inline CSS (no framework) | Styling |

---

## Developer Notes

### Data quality
- Scrape individual salon websites for real price data instead of estimating/guessing from Google's `priceLevel` enum

### Backend
- Migrate from `RestTemplate` to `WebClient` for non-blocking HTTP calls during seeding
- Add Spring Security with JWT authentication so only salon owners can edit their own records
- Add proper pagination to `GET /api/v1/salons` (`Pageable`) instead of returning the full list
- Store the Google Places API key in a secrets manager (e.g. AWS Secrets Manager) rather than environment variables
- Add tests for the backend services

### Frontend
- Build with a proper component library and better styling
- Add a map view using Google Maps JS SDK to show salon locations visually
- Add district and service filter dropdowns on the list view
- Client-side caching with React Query to avoid re-fetching on every render

### Infrastructure
- Set up a CI/CD pipeline (GitHub Actions) to build, test, and push the Docker image automatically
- Add a Nginx container to serve the React frontend and proxy `/api` to the Spring Boot container in a single `docker compose up`
