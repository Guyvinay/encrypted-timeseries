# Encrypted Streaming Pipeline — Java Spring Boot Implementation

This project implements a backend streaming system that generates encrypted data streams over sockets, validates and decrypts incoming messages, persists time-bucketed records into a database, and exposes REST APIs for real-time data consumption.

The solution is fully containerized and can be started with a single Docker Compose command.

---

# Problem Summary

The system consists of two backend services:

## 1) Emitter Service

- Periodically generates encrypted message streams
- Sends encrypted data over TCP sockets every 10 seconds
- Each stream contains 49–499 encrypted messages
- Payload fields:
    - name
    - origin
    - destination
    - secret_key (SHA-256 checksum)
- Encryption algorithm: AES-256-CTR
- Messages are separated using pipe (`|`) delimiter

## 2) Listener Service

- Accepts socket connections
- Decrypts incoming message stream
- Validates SHA-256 checksum for integrity
- Discards corrupted messages
- Groups valid messages into minute-based time buckets
- Persists aggregated records into PostgreSQL
- Exposes REST APIs for frontend consumption

---

# Project Structure

```
project-root
│
├── docker-compose.yml
├── README.md
│
├── emitter-service.jar
├── listener-service.jar
│
├── Dockerfile
```

> Both services use pre-built executable JAR files packaged into Docker images.

---

# Prerequisites

Install the following:

- Docker 24+
- Docker Compose v2+
- Git

Required ports:

| Port | Purpose |
|------|---------|
| 5432 | PostgreSQL |
| 8082 | Listener REST API |
| 9000 | Listener TCP Socket Server |

---

# How To Run The Project

## Step 1 — Clone Repository

```bash
git clone <your-repo-url>
cd <project-root>
docker-compose build
docker-compose up -d
docker ps
docker logs listener-service
```

Access APIs:

- Latest stats: `http://localhost:8080/api/stats/latest`
- Latest records: `http://localhost:8080/api/records/latest`