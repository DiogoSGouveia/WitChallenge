# Calculator Microservice Project

This project implements a distributed calculator using microservices architecture with Spring Boot and Kafka. It consists of two main services: a REST API service and a Calculator service, communicating through Kafka messages.

## Project Structure

WitChallenge/
├── calculator/ # Calculator service
├── rest/ # REST API service
├── shared/ # Shared DTOs and utilities
├── logs/ # Log files directory
│ ├── calculator/ # Calculator service logs
│ └── rest/ # REST service logs
└── docker-compose.yml

## Prerequisites

- Java 21 or higher
- Docker and Docker Compose
- Gradle
- Postman

## Building the Project

1. Clone the repository:

- git clone <repository-url>
- cd WitChallenge

2. Build the Project using Gradle:

- ./gradlew clean build

3. Build the Docker compose:

- docker-compose build

## Run the Application:

1. Start all services using Docker Compose:

- docker-compose up -d

2. Verify that all services are running:

- docker-compose ps

## Testing the Calculator

You can use Postman to test the calculator.

### Addition

1. Send a GET request to http://localhost:8082/api/calculator/sum?a=1&b=2

2. The response should be a JSON object with the result of the addition.

### Subtraction

1. Send a GET request to http://localhost:8082/api/calculator/subtract?a=1&b=2
2. The response should be a JSON object with the result of the subtraction.

### Multiplication

1. Send a GET request to http://localhost:8082/api/calculator/multiply?a=1&b=2
2. The response should be a JSON object with the result of the multiplication.

### Division

1. Send a GET request to http://localhost:8082/api/calculator/divide?a=1&b=2
2. The response should be a JSON object with the result of the division.

### Example Response

HTTP/1.1 200 OK
Content-Type: application/json
Request-ID: 550e8400-e29b-41d4-a716-446655440000
{
"result": 3,
"error": null
}

## Logging

The project uses Logback for logging. The logs are stored in the logs/ directory, and the Calculator service logs are stored in the logs/calculator/ directory, and the REST service logs are stored in the logs/rest/ directory.

## Stopping the Application

To stop all services:

- docker-compose down

## Features

- Distributed calculator using microservices
- Kafka message broker for inter-service communication
- Request ID tracking across services
- MDC logging for request tracing
- Rolling log files with 30-day retention
- Docker containerization
- Load-balanced REST API

## Troubleshooting

1. If services fail to start, check:

   - Docker daemon is running
   - Ports 8081, 8082, 9092 are available
   - Sufficient disk space for logs

2. If logs are not generating:
   - Check write permissions on the logs directory
   - Verify volume mounts in docker-compose.yml
   - Check service logs using `docker compose logs`
