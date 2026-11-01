# TaskForge Worker Service

Stateless task execution engine microservice for the TaskForge workflow platform.

## Features
- **Task Execution Handlers:**
  - `CSV_VALIDATE`: Validates headers and rows against expected schema.
  - `DATA_TRANSFORM`: Maps and transforms payload fields.
  - `CONDITION`: Evaluates boolean branch expressions.
  - `GENERATE_REPORT`: Generates execution summaries and reports.
  - `SEND_EMAIL`: Formats and delivers notifications.
- **Kafka Worker Consumer:** Listens on `task.requested.v1` and emits `task.completed.v1` / `task.failed.v1`.

## Tech Stack
- Java 21 & Spring Boot 3.3.4
- Spring Kafka
- Spring Boot Actuator
- Lombok

## Getting Started

### Prerequisites
- JDK 21
- Apache Kafka

### Running Locally
```bash
./mvnw spring-boot:run
```

Or build the runnable jar:
```bash
./mvnw clean package
java -jar target/taskforge-worker-service-0.1.0-SNAPSHOT.jar
```

### Docker
```bash
docker build -t taskforge-worker-service .
docker run -p 8082:8082 taskforge-worker-service
```
