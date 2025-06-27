# 🚨 Error Handling Examples

## Overview
This document provides comprehensive examples of error responses from the Book Management API, demonstrating proper RESTful error handling practices.

## Error Response Structure

All error responses follow a consistent structure:

```json
{
  "status": 400,
  "message": "Error Type",
  "details": "Detailed error description",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/v1/books",
  "validationErrors": ["field: error message"] // Only for validation errors
}
```

## 🔴 Client Errors (4xx)

### 400 Bad Request - Validation Errors

**Scenario**: Creating a book with invalid data
```bash
POST /api/v1/books
Content-Type: application/json

{
  "title": "",
  "author": "",
  "isbn": "invalid-isbn",
  "price": -10.0,
  "stockQuantity": -5,
  "publishedYear": 3000
}
```

**Response**:
```json
{
  "status": 400,
  "message": "Validation Failed",
  "details": "Request validation failed",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/v1/books",
  "validationErrors": [
    "title: Title is required",
    "author: Author is required", 
    "isbn: Invalid ISBN format",
    "price: Price must be greater than 0",
    "stockQuantity: Stock quantity cannot be negative",
    "publishedYear: Published year cannot be in the future"
  ]
}
```

### 400 Bad Request - Business Logic Validation

**Scenario**: Creating a book with duplicate ISBN
```bash
POST /api/v1/books
Content-Type: application/json

{
  "title": "Another Book",
  "author": "Another Author",
  "isbn": "978-0-7432-7356-5", // Already exists
  "publishedYear": 2023,
  "price": 29.99,
  "stockQuantity": 100
}
```

**Response**:
```json
{
  "status": 400,
  "message": "Validation Error",
  "details": "Book with ISBN 978-0-7432-7356-5 already exists",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/v1/books"
}
```

### 404 Not Found

**Scenario**: Requesting a non-existent book
```bash
GET /api/v1/books/99999
```

**Response**:
```json
{
  "status": 404,
  "message": "Book Not Found",
  "details": "Book not found with ID: 99999",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/v1/books/99999"
}
```

**Scenario**: Updating a non-existent book
```bash
PUT /api/v1/books/99999
Content-Type: application/json

{
  "title": "Updated Title",
  "author": "Updated Author",
  "isbn": "978-0-111111-11-1",
  "price": 35.99,
  "stockQuantity": 150
}
```

**Response**:
```json
{
  "status": 404,
  "message": "Book Not Found", 
  "details": "Book not found with ID: 99999",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/v1/books/99999"
}
```

### 409 Conflict - Data Integrity Violation

**Scenario**: Database constraint violation (duplicate unique key)
```bash
POST /api/v1/books
Content-Type: application/json

{
  "title": "Test Book",
  "author": "Test Author", 
  "isbn": "978-0-7432-7356-5", // Duplicate ISBN at database level
  "publishedYear": 2023,
  "price": 29.99,
  "stockQuantity": 100
}
```

**Response**:
```json
{
  "status": 409,
  "message": "Data Integrity Violation",
  "details": "Duplicate entry - resource already exists",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/v1/books"
}
```

## 🔴 Server Errors (5xx)

### 500 Internal Server Error

**Scenario**: Unexpected server error
```bash
GET /api/v1/books/1
```

**Response** (when unexpected error occurs):
```json
{
  "status": 500,
  "message": "Internal Server Error",
  "details": "An unexpected error occurred",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/v1/books/1"
}
```

## 🟢 Success Responses

### 200 OK - Successful Retrieval
```json
{
  "id": 1,
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald", 
  "isbn": "978-0-7432-7356-5",
  "publishedYear": 1925,
  "description": "A classic American novel",
  "price": 29.99,
  "stockQuantity": 100,
  "createdAt": "2024-01-15T10:00:00",
  "updatedAt": "2024-01-15T10:00:00"
}
```

### 201 Created - Successful Creation
```json
{
  "id": 1,
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "isbn": "978-0-7432-7356-5", 
  "publishedYear": 1925,
  "description": "A classic American novel",
  "price": 29.99,
  "stockQuantity": 100,
  "createdAt": "2024-01-15T10:00:00",
  "updatedAt": "2024-01-15T10:00:00"
}
```

### 204 No Content - Successful Deletion
```
HTTP/1.1 204 No Content
Date: Mon, 15 Jan 2024 10:30:00 GMT
```

## 📝 Testing Error Scenarios

### Using cURL

#### Test Validation Error
```bash
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "",
    "author": "",
    "isbn": "invalid",
    "price": -10,
    "stockQuantity": -5
  }'
```

#### Test Not Found Error
```bash
curl -X GET http://localhost:8080/api/v1/books/99999
```

#### Test Duplicate ISBN Error
```bash
# First, create a book
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Book",
    "author": "Test Author",
    "isbn": "978-0-123456-78-9",
    "publishedYear": 2023,
    "price": 29.99,
    "stockQuantity": 100
  }'

# Then try to create another with same ISBN
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Another Book",
    "author": "Another Author", 
    "isbn": "978-0-123456-78-9",
    "publishedYear": 2023,
    "price": 35.99,
    "stockQuantity": 50
  }'
```

### Using Postman

The provided Postman collection includes dedicated requests in the **"❌ Error Examples"** folder:

1. **Validation Error - Invalid Book**: Tests field validation
2. **Not Found Error**: Tests 404 response
3. **Duplicate ISBN Error**: Tests business logic validation

## 🔧 Error Handling Best Practices Demonstrated

### ✅ Consistent Error Structure
- All errors follow the same JSON structure
- Predictable field names and types
- Timestamp for debugging

### ✅ Appropriate HTTP Status Codes
- `400`: Client input errors
- `404`: Resource not found
- `409`: Data conflicts
- `500`: Server errors

### ✅ Detailed Error Messages
- `message`: High-level error category
- `details`: Specific error description
- `validationErrors`: Field-level validation details

### ✅ Request Context
- `path`: The endpoint that caused the error
- `timestamp`: When the error occurred

### ✅ Security Considerations
- No sensitive data exposed in error messages
- No stack traces in production responses
- Consistent error format prevents information leakage

## 🐛 Debugging Tips

### For Developers
1. **Check application logs** for detailed stack traces
2. **Use H2 console** to verify database state
3. **Enable debug logging** in `application.yml`
4. **Use Postman tests** to automate error scenario validation

### For API Consumers
1. **Always check status codes** before processing responses
2. **Parse error responses** to show meaningful messages to users
3. **Handle validation errors** by highlighting specific fields
4. **Implement retry logic** for 5xx errors

## 📚 Related Documentation

- [OpenAPI Specification](http://localhost:8080/swagger-ui.html) - Interactive API documentation
- [Postman Collection](../postman/README.md) - Automated testing examples
- [Spring Boot Documentation](https://spring.io/projects/spring-boot) - Framework reference

---

**Note**: All examples use the default development server running on `http://localhost:8080`