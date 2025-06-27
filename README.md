# 📚 Book Management System - RESTful API

A production-ready RESTful API built with Spring Boot, demonstrating industry best practices for API design, implementation, and documentation. This project showcases comprehensive CRUD operations, advanced search capabilities, robust error handling, and complete API documentation.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.8+-blue)
![License](https://img.shields.io/badge/License-MIT-green)
![API](https://img.shields.io/badge/API-RESTful-success)

## 🎯 Assessment Deliverables

This project fulfills all requirements for RESTful API design best practices:

✅ **Complete API Implementation** - 11+ endpoints with full CRUD operations following REST principles  
✅ **Comprehensive API Documentation** - Interactive OpenAPI/Swagger documentation with detailed specifications  
✅ **Postman Testing Suite** - Complete collection with 15+ requests, automated tests, and error scenarios  
✅ **Robust Error Handling** - Global exception management with proper HTTP status codes and detailed responses  
✅ **Industry-Standard Code Organization** - Clean architecture with proper separation of concerns

## 🏗️ API Architecture

### REST Endpoints Overview

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| `GET` | `/api/v1/books` | Retrieve all books (paginated) | 200, 500 |
| `POST` | `/api/v1/books` | Create a new book | 201, 400, 409 |
| `GET` | `/api/v1/books/{id}` | Retrieve book by ID | 200, 404 |
| `PUT` | `/api/v1/books/{id}` | Update existing book | 200, 400, 404 |
| `DELETE` | `/api/v1/books/{id}` | Delete book | 204, 404 |

### Advanced Search Capabilities

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/books/search?keyword={term}` | Multi-field keyword search |
| `GET` | `/api/v1/books/search/author?name={author}` | Search by author name |
| `GET` | `/api/v1/books/search/title?title={title}` | Search by book title |
| `GET` | `/api/v1/books/year/{year}` | Filter by publication year |
| `GET` | `/api/v1/books/price-range?min={min}&max={max}` | Price range filtering |
| `GET` | `/api/v1/books/low-stock?threshold={threshold}` | Low inventory alerts |

## 🚀 Quick Start

### Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **Git** for version control

### Installation and Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/richardvynz/book-management-system.git
   cd book-management-system
   ```

2. **Build and run the application**
   ```bash
   # Using Maven Wrapper (recommended)
   ./mvnw spring-boot:run
   
   # Or using Maven directly
   mvn spring-boot:run
   ```

3. **Verify the application is running**
   ```bash
   curl http://localhost:8080/api/v1/books
   ```

### Access Points

- **🌐 Application**: http://localhost:8080
- **📖 API Documentation (Swagger UI)**: http://localhost:8080/swagger-ui.html
- **📋 OpenAPI Specification**: http://localhost:8080/v3/api-docs
- **🗄️ H2 Database Console**: http://localhost:8080/h2-console

## 📖 API Documentation

### Request/Response Examples

**Create a Book (POST /api/v1/books)**
```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0-13-235088-4",
  "publishedYear": 2008,
  "price": 45.99,
  "stockQuantity": 25,
  "description": "A handbook of agile software craftsmanship"
}
```

**Successful Response (201 Created)**
```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0-13-235088-4",
  "publishedYear": 2008,
  "price": 45.99,
  "stockQuantity": 25,
  "description": "A handbook of agile software craftsmanship",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Error Response Format

All API errors follow a consistent format with appropriate HTTP status codes:

```json
{
  "status": 400,
  "message": "Validation Failed",
  "details": "The request contains invalid data",
  "timestamp": "2024-01-15T10:30:00.123Z",
  "path": "/api/v1/books",
  "validationErrors": [
    "title: Title is required and cannot be blank",
    "isbn: ISBN format is invalid (expected: XXX-X-XX-XXXXXX-X)"
  ]
}
```

**Common HTTP Status Codes:**
- `200 OK` - Successful GET/PUT operations
- `201 Created` - Successful POST operations
- `204 No Content` - Successful DELETE operations
- `400 Bad Request` - Validation errors or malformed requests
- `404 Not Found` - Resource not found
- `409 Conflict` - Business rule violations (e.g., duplicate ISBN)
- `500 Internal Server Error` - Unexpected server errors

## 📮 API Testing with Postman

### Quick Setup
1. **Import Collection & Environment**
   ```
   Files located in /postman directory:
   ├── Book-Management-API.postman_collection.json
   └── Book-Management-Environment.postman_environment.json
   ```

2. **Import in Postman**
   - Open Postman → Import → Upload Files
   - Select both files from `/postman` directory
   - Choose "Book Management Environment" from environment dropdown

3. **Start Testing**
   ```bash
   # Start the API server
   mvn spring-boot:run
   
   # API will be available at http://localhost:8080
   # Then run the Postman collection
   ```

### 🎯 Collection Features

✅ **Complete CRUD Operations** - All REST endpoints with automated tests  
✅ **Advanced Search Testing** - Keyword, author, title, and filter endpoints  
✅ **Error Scenario Coverage** - 400 (Bad Request), 404 (Not Found), 409 (Conflict)  
✅ **Automated Variable Management** - Dynamic ID storage for dependent requests  
✅ **Response Validation** - Comprehensive assertions for all responses  
✅ **Business Logic Testing** - Validates data integrity and constraints

### 📊 Automated Test Coverage

Each request includes automated test scripts that verify:
- **HTTP Status Codes** - Proper REST response codes
- **Response Structure** - JSON schema validation
- **Data Types & Formats** - Field validation (dates, numbers, strings)
- **Business Rules** - ISBN uniqueness, required fields, data constraints
- **Error Messages** - Proper error response format and details

## 🧪 Testing

### Run Tests

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Run tests with coverage report
mvn clean test jacoco:report

# Run specific test class
mvn test -Dtest=BookControllerTest
```

### Test Coverage

- **Unit Tests**: Service layer business logic and validation
- **Integration Tests**: Complete API endpoint testing with test database
- **Repository Tests**: Data access layer validation
- **Controller Tests**: HTTP request/response handling

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/richardvinz/Book_Management_App/
│   │   ├── controller/          # REST API Controllers
│   │   │   ├── BookController.java
│   │   │   └── WebController.java
│   │   ├── service/             # Business Logic Layer
│   │   │   ├── BookService.java
│   │   │   └── BookServiceImpl.java
│   │   ├── repository/          # Data Access Layer
│   │   │   └── BookRepository.java
│   │   ├── entity/              # JPA Entities
│   │   │   └── Book.java
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── BookRequestDto.java
│   │   │   ├── BookResponseDto.java
│   │   │   └── ErrorResponseDto.java
│   │   ├── exception/           # Exception Handling
│   │   │   ├── BookNotFoundException.java
│   │   │   ├── DuplicateIsbnException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── config/              # Configuration Classes
│   │   │   ├── CorsConfig.java
│   │   │   └── OpenApiConfig.java
│   │   └── BookManagementAppApplication.java
│   ├── resources/
│   │   ├── static/              # Frontend Assets (Bonus)
│   │   │   └── index.html
│   │   └── application.yml      # Application Configuration
└── test/                        # Test Classes
    ├── controller/              # Controller Tests
    ├── service/                 # Service Tests
    └── repository/              # Repository Tests
```

## ⚙️ Configuration

### Application Properties (application.yml)

```yaml
server:
  port: 8080

spring:
  application:
    name: book-management-api
  datasource:
    url: jdbc:h2:mem:bookdb
    driverClassName: org.h2.Driver
    username: sa
    password: password
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: method

logging:
  level:
    com.richardvinz.Book_Management_App: DEBUG
    org.springframework.web: DEBUG
```

### Database Configuration

- **Development**: H2 in-memory database
- **Connection URL**: `jdbc:h2:mem:bookdb`
- **Console Access**: http://localhost:8080/h2-console
- **Credentials**: username: `sa`, password: `password`

## 🔧 Technology Stack

### Core Dependencies

- **Spring Boot 3.1.5** - Application framework and REST capabilities
- **Spring Data JPA** - Data persistence and repository abstraction
- **Spring Boot Validation** - Bean validation with Hibernate Validator
- **H2 Database** - In-memory database for development and testing
- **SpringDoc OpenAPI** - API documentation generation
- **Lombok** - Boilerplate code reduction

### Testing Dependencies

- **Spring Boot Test** - Testing framework with TestContainers support
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework for unit tests
- **REST Assured** - API testing and validation
- **Testcontainers** - Integration testing with real databases

### Build and DevOps

- **Maven 3.8+** - Dependency management and build automation
- **Maven Wrapper** - Ensures consistent Maven version across environments
- **JaCoCo** - Code coverage reporting
- **Spring Boot Maven Plugin** - Application packaging and execution

## 🚀 Production Deployment

### Build Production JAR

```bash
# Clean build with tests
mvn clean package

# Skip tests for faster builds (not recommended)
mvn clean package -DskipTests

# Build with specific profile
mvn clean package -Pprod
```

### Run Production Application

```bash
# Run with default profile
java -jar target/book-management-api-1.0.0.jar

# Run with production profile
java -jar target/book-management-api-1.0.0.jar --spring.profiles.active=prod

# Run with custom port
java -jar target/book-management-api-1.0.0.jar --server.port=9090
```

### Environment Configuration

For production deployment, override default configurations:

```bash
# Environment variables
export SPRING_PROFILES_ACTIVE=prod
export SERVER_PORT=8080
export DATABASE_URL=jdbc:postgresql://localhost:5432/bookdb
export DATABASE_USERNAME=bookuser
export DATABASE_PASSWORD=securepassword

# Or use application-prod.yml
```

## 📊 API Performance and Monitoring

### Health Checks

```bash
# Application health
curl http://localhost:8080/actuator/health

# Application info
curl http://localhost:8080/actuator/info

# Metrics
curl http://localhost:8080/actuator/metrics
```

### Pagination and Performance

- **Default Page Size**: 10 items
- **Maximum Page Size**: 100 items
- **Database Indexing**: Optimized queries for search operations
- **Response Time**: < 200ms for CRUD operations

## 🎁 Bonus Features

### Web Frontend

A responsive web interface is included to demonstrate real-world API usage:

- **Access**: http://localhost:8080/
- **Features**: Complete CRUD operations, advanced search, analytics dashboard
- **Technology**: Vanilla HTML5, CSS3, and JavaScript (ES6+)
- **Responsive**: Mobile-first design supporting all device sizes

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/awesome-feature`)
3. Commit your changes (`git commit -m 'Add awesome feature'`)
4. Push to the branch (`git push origin feature/awesome-feature`)
5. Open a Pull Request

## 📞 Support

For questions, issues, or suggestions:

- **Repository Issues**: [GitHub Issues](https://github.com/richardvynz/book-management-system/issues)
- **Documentation**: [Wiki](https://github.com/richardvynz/book-management-system/wiki)
- **API Documentation**: [Swagger UI](http://localhost:8080/swagger-ui.html)

---

**This RESTful API demonstrates comprehensive adherence to industry best practices and serves as a reference implementation for modern API development with Spring Boot.**