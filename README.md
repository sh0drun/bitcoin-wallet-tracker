# Bitcoin Wallet Tracker

A Bitcoin wallet tracking system that records BTC transactions and provides historical balance data at hourly intervals.

## Technology Stack

- Kotlin 1.9.25
- Spring Boot 3.5.7
- PostgreSQL 16
- Gradle 8.14.3

## Prerequisites

- Java 17+
- Docker & Docker Compose

## Quick Start

### 1. Start the database
```bash
docker-compose up -d postgres
```

### 2. Build and run in terminal
```bash
./gradlew build
java -jar build/libs/bitcoin-wallet-tracker-0.0.1-SNAPSHOT.jar
```

Or build and bootJar from IDE

Application starts at `http://localhost:8080`

## API Endpoints

### Save Transaction
```bash
POST /api/transactions
Content-Type: application/json

{
  "datetime": "2019-10-05T14:48:01+07:00",
  "amount": 1.1
}
```

### Get Balance History
```bash
GET /api/transactions/history?startDatetime=2019-10-05T12:00:00Z&endDatetime=2019-10-05T16:00:00Z
```

## Run Tests
```bash
./gradlew test
```

## Docker Deployment
```bash
docker-compose up --build
```

## Notes

- Initial balance: 1000 BTC
- All datetimes stored in UTC
- Balance calculated at end of each hour
