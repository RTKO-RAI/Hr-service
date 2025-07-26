# HR-Service

A Spring Boot microservice for managing employee leave requests. This service provides endpoints to create, manage, and query leave requests, track remaining leave days, and enforce leave policies.

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Spring Validation
- Swagger / OpenAPI
- Maven

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL installed and running

---

### 📦 Configuration

Before running the application, configure your PostgreSQL database credentials:

> File: `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_hr_service_db
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
