# Split Bill System

Microservice-based split bill application built using Spring Boot and Java 21.

This project provides:
- Authentication Service
- Expense Management Service
- Settlement Calculation Service

The system supports:
- JWT token validation
- group creation
- participant management
- expense splitting
- settlement calculation
- debt summary

---

# Architecture

```text
Client
   |
   v
auth-service
   |
   v
expense-service  ---> settlement-service
```

---

# Tech Stack

## Backend
- Java 21
- Spring Boot 3
- Spring Validation
- Spring Data JPA
- OpenFeign
- REST API

## Database
- PostgreSQL

## Documentation
- Swagger OpenAPI

## Containerization
- Docker
- Docker Compose

---

# Services

| Service | Port | Description |
|---|---|---|
| auth-service | 8081 | Authentication & token validation |
| expense-service | 8082 | Expense & group management |
| settlement-service | 8083 | Settlement & debt calculation |

---

# Features

## Authentication Service
- Validate JWT token
- Secure inter-service communication

---

## Expense Service
- Create group
- Add participant
- Create expense
- Equal split
- Exact split
- Percentage split
- Expense validation
- Financial summary

---

## Settlement Service
- Calculate settlement
- Calculate debt summary
- Determine who owes who
- Settlement recommendation

---

# Swagger Documentation

## Authentication API

```text
http://localhost:8081/swagger-ui/index.html#/Authentication%20API/validateToken
```

---

## Expense API

```text
http://localhost:8082/swagger-ui/index.html#/Expense%20API/getSummary
```

---

## Settlement API

```text
http://localhost:8083/swagger-ui/index.html#/Settlement/getSettlement
```

---

# Microservice Communication

## auth-service
Responsible for:
- authentication
- token validation
- authorization support

---

## expense-service
Responsible for:
- group management
- participant management
- expense management
- split calculation

---

## settlement-service
Responsible for:
- debt aggregation
- settlement calculation
- financial summary

Uses:
- OpenFeign
- REST communication

---

# Project Structure

```text
splitbill-system
│
├── auth-service
│
├── expense_service
│
├── settlement-service
│
├── docker-compose.yml
│
├── README.md
│
└── .gitignore
```

---

# Running Project

## 1. Clone Repository

```bash
git clone https://github.com/aji2111/splitbill-system.git
```

---

## 2. Run Docker Compose

```bash
docker compose up --build
```

---

# Docker Services

| Container | Port |
|---|---|
| auth-service | 8081 |
| expense-service | 8082 |
| settlement-service | 8083 |
| postgres | 5432 |

---

# Example APIs

# Create Group

```http
POST /api/v1/groups
```

Request:

```json
{
  "name": "Trip Bali and Lombok",
  "participants": [
    {
      "name": "Wahyu"
    },
    {
      "name": "Budi"
    },
    {
      "name": "Andi"
    }
  ]
}
```

---

# Create Expense

```http
POST /api/v1/expenses
```

Request:

```json
{
  "title": "Dinner Seafood Jimbaran",
  "description": "Dinner bersama di Jimbaran Bali",
  "amount": 300000,
  "paidByParticipantId": "participant-id",
  "splitType": "EQUAL",
  "splits": [
    {
      "participantId": "participant-id"
    }
  ]
}
```

---

# Settlement Summary

```http
GET /api/v1/groups/{groupId}/settlements
```

Response:

```json
{
  "groupId": "340db037-9dc3-4f72-a5f6-2fdb38dc9983",
  "groupName": "Trip Bali and Lombok",
  "totalExpense": 900000,
  "totalParticipants": 3,
  "settlements": [
    {
      "from": "Budi",
      "to": "Wahyu",
      "amount": 150000
    },
    {
      "from": "Andi",
      "to": "Wahyu",
      "amount": 50000
    }
  ]
}
```

---

# Technical Highlights

This project demonstrates:

- Spring IoC
- Java Stream API
- Intermediate Native SQL Query
- REST Communication
- OpenFeign
- Docker Containerization
- Microservice Architecture
- Financial Settlement Algorithm
- Clean Architecture
- Layered Architecture
- DTO Projection
- Swagger Documentation

---

# Native SQL Example

```sql
 SELECT

            p.public_id AS participantId,

            p.name AS participantName,

            COALESCE(
                SUM(
                    CASE
                        WHEN e.paid_by = p.id
                        THEN e.total_amount
                        ELSE 0
                    END
                ),
                0
            ) AS totalPaid,

            COALESCE(
                SUM(es.share_amount),
                0
            ) AS totalShare,

            COALESCE(
                SUM(
                    CASE
                        WHEN e.paid_by = p.id
                        THEN e.total_amount
                        ELSE 0
                    END
                ),
                0
            )
            -
            COALESCE(
                SUM(es.share_amount),
                0
            ) AS balance

        FROM participants p

        JOIN expense_splits es
            ON es.participant_id = p.id

        JOIN expenses e
            ON e.id = es.expense_id

        JOIN bill_groups bg
            ON bg.id = e.group_id

        WHERE bg.public_id = :groupPublicId

        GROUP BY
            p.public_id,
            p.name

        ORDER BY balance DESC
```

---

# Future Improvements

- JWT Authentication Enhancement
- Redis Caching
- Kafka Event Streaming
- Notification Service
- API Gateway
- Service Discovery
- Kubernetes Deployment

---

# Author

Wahyu Aji Saputro

Backend Engineer | Java Spring Boot Developer
