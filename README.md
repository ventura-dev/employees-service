# Employees Service

REST API for employee management built with **Spring Boot 2.7.18**, **Java 17**, **MySQL 8**, **Docker**, and **Docker Compose**.

## Features

- CRUD operations for employees
- Request validation with `@Valid`
- Global exception handling for validation errors
- Swagger / OpenAPI documentation
- Spring Boot Actuator health and metrics endpoints
- Request header logging with filter
- Unit tests with JUnit 5 and Mockito
- Code coverage report with JaCoCo

---

## Tech Stack

- Java 17
- Spring Boot 2.7.18
- Spring Web
- Spring Data JPA
- Spring Validation
- MySQL 8
- Lombok
- MapStruct
- Springdoc OpenAPI
- Spring Boot Actuator
- JUnit 5 / Mockito
- Docker / Docker Compose

---

## API Endpoints

Base path:

```text
/employees
```

- `GET /employees` → Get all employees
- `GET /employees/{id}` → Get employee by ID
- `POST /employees` → Create one or multiple employees
- `PUT /employees/{id}` → Update an employee
- `DELETE /employees/{id}` → Delete an employee
- `GET /employees/search?name={name}` → Search employees by name

---

## Example Request (POST /employees)

```json
{
  "employees": [
    {
      "firstName": "Jorge",
      "secondName": "Luis",
      "lastNamePaternal": "Ventura",
      "lastNameMaternal": "Almazan",
      "age": 30,
      "sex": "M",
      "birthDate": "1994-05-10",
      "position": "Backend Developer",
      "active": true
    }
  ]
}
```

---

## Run Locally

### 1. Create the database

```sql
CREATE DATABASE invex_db;
```

### 2. Configure `application.yml`

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3307/invex_db?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:root123}
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

---

## Run with Docker

### Build image

```bash
docker build -t employees-service .
```

### Run container

Run with local MySQL database

```bash
docker run -d \
  --name employees-service \
  -p 8080:8080 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3307/invex_db?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true" \
  -e DB_USERNAME="root" \
  -e DB_PASSWORD="root123" \
  employees-service
```

---

## Run with Docker Compose

Run with container MySQL database with port external 3307


### `docker-compose.yml`

```yaml
services:
  mysql8:
    image: mysql:8.0
    container_name: mysql8
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: invex_db
    ports:
      - "3307:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  employees-service:
    image: jacmen2005/employees-service:latest
    container_name: employees-service
    restart: always
    depends_on:
      - mysql8
    ports:
      - "8080:8080"
    environment:
      DB_URL: jdbc:mysql://mysql8:3306/invex_db?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true
      DB_USERNAME: root
      DB_PASSWORD: root123

volumes:
  mysql_data:
```

### Start services

```bash
docker compose up -d
```

### Stop services

```bash
docker compose down
```

---

## Swagger / OpenAPI

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

---

## Actuator

Available endpoints:

- `/actuator/health`
- `/actuator/metrics`

Example:

```text
http://localhost:8080/actuator/health
```

---

## Testing

Run tests:

```bash
mvn test
```

---

## JaCoCo Coverage

Generate coverage report:

```bash
mvn clean test
```

Report location:

```text
target/site/jacoco/index.html
```

---

## Docker Hub Image

Public image available at:

```text
jacmen2005/employees-service
```

Pull it with:

```bash
docker pull jacmen2005/employees-service:latest
```

---

## Author

**Jorge Ventura Hernández Almazan**
