# 📚 Book Management System

A comprehensive full-stack web application for managing book inventory with a modern REST API and intuitive frontend interface.

## 🌟 Features

### 📊 **Dashboard & Analytics**
- Real-time statistics (total books, low stock alerts, inventory value)
- Recent books overview
- Low stock monitoring with customizable thresholds
- Visual status indicators for stock levels

### 🔍 **Advanced Search & Filtering**
- Keyword search across title, author, and description
- Search by specific author or title
- Filter by publication year
- Price range filtering
- Real-time search results

### 📖 **Book Management**
- Complete CRUD operations (Create, Read, Update, Delete)
- Form validation with detailed error messages
- Bulk operations support
- Pagination and sorting capabilities

### 🎨 **Modern User Interface**
- Responsive design for desktop and mobile
- Professional dark/light theme
- Intuitive navigation with tab-based interface
- Real-time connection status monitoring
- Keyboard shortcuts for power users

## 🛠 **Technical Stack**

### **Backend**
- **Framework**: Spring Boot 3.x
- **Database**: H2 Database (in-memory for development)
- **ORM**: JPA/Hibernate
- **API Documentation**: Swagger/OpenAPI 3
- **Build Tool**: Maven
- **Java Version**: 17+

### **Frontend**
- **Languages**: HTML5, CSS3, Vanilla JavaScript
- **Styling**: Modern CSS with CSS Variables
- **Design**: Responsive Grid/Flexbox Layout
- **Icons**: Unicode Emojis for universal compatibility

### **API Features**
- RESTful endpoints following industry standards
- Comprehensive error handling
- CORS configuration for cross-origin requests
- Input validation and sanitization
- Paginated responses for large datasets

## 🚀 **Quick Start**

### **Prerequisites**
- Java 17 or higher
- Maven 3.6+
- Git

### **Installation & Setup**

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/book-management-system.git
   cd book-management-system
   ```

2. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. **Access the application**
   - **Frontend**: http://localhost:8080/
   - **API Documentation**: http://localhost:8080/swagger-ui.html
   - **H2 Database Console**: http://localhost:8080/h2-console

### **Default Database Configuration**
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: *(empty)*

## 📚 **API Documentation**

### **Base URL**: `http://localhost:8080/api/v1`

### **Core Endpoints**

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/books` | Get all books (paginated) |
| `GET` | `/books/{id}` | Get book by ID |
| `POST` | `/books` | Create new book |
| `PUT` | `/books/{id}` | Update existing book |
| `DELETE` | `/books/{id}` | Delete book |

### **Search Endpoints**

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/books/search?keyword={term}` | Search books by keyword |
| `GET` | `/books/search/author?author={name}` | Find books by author |
| `GET` | `/books/search/title?title={title}` | Find books by title |
| `GET` | `/books/year/{year}` | Find books by publication year |
| `GET` | `/books/price-range?minPrice={min}&maxPrice={max}` | Find books by price range |
| `GET` | `/books/low-stock?threshold={number}` | Get low stock books |

### **Query Parameters**
- `page`: Page number (0-based)
- `size`: Page size (default: 10)
- `sortBy`: Sort field (title, author, price, publishedYear, stockQuantity)
- `sortDir`: Sort direction (asc, desc)

### **Example API Calls**

```bash
# Get all books with pagination
curl "http://localhost:8080/api/v1/books?page=0&size=10&sortBy=title&sortDir=asc"

# Search for books
curl "http://localhost:8080/api/v1/books/search?keyword=java&page=0&size=5"

# Create a new book
curl -X POST "http://localhost:8080/api/v1/books" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "publishedYear": 2008,
    "price": 39.99,
    "stockQuantity": 25,
    "description": "A Handbook of Agile Software Craftsmanship"
  }'
```

## 🗂 **Project Structure**

```
book-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/bookmanagement/
│   │   │       ├── BookManagementApplication.java
│   │   │       ├── config/
│   │   │       │   ├── CorsConfig.java
│   │   │       │   └── SwaggerConfig.java
│   │   │       ├── controller/
│   │   │       │   └── BookController.java
│   │   │       ├── dto/
│   │   │       │   └── BookDTO.java
│   │   │       ├── entity/
│   │   │       │   └── Book.java
│   │   │       ├── exception/
│   │   │       │   ├── BookNotFoundException.java
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── repository/
│   │   │       │   └── BookRepository.java
│   │   │       └── service/
│   │   │           ├── BookService.java
│   │   │           └── BookServiceImpl.java
│   │   └── resources/
│   │       ├── static/
│   │       │   └── index.html
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/example/bookmanagement/
│               ├── BookManagementApplicationTests.java
│               ├── controller/
│               │   └── BookControllerTest.java
│               └── service/
│                   └── BookServiceTest.java
├── target/
├── pom.xml
└── README.md
```

## 🔧 **Configuration**

### **Application Properties**
```properties
# Server Configuration
server.port=8080

# Database Configuration (H2 In-Memory)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console (Development only)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Swagger Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

## 🧪 **Testing**

### **Run Tests**
```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=BookControllerTest
```

### **Test Coverage**
- Unit tests for service layer
- Integration tests for REST endpoints
- Repository tests for data access
- Validation tests for input handling

## 🚀 **Deployment**

### **Development**
```bash
mvn spring-boot:run
```

### **Production Build**
```bash
mvn clean package
java -jar target/book-management-system-1.0.0.jar
```

### **Docker (Optional)**
```dockerfile
FROM openjdk:17-jre-slim
COPY target/book-management-system-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🎯 **Usage Examples**

### **Frontend Interface**
1. Open http://localhost:8080/ in your browser
2. Use the dashboard to view statistics and recent books
3. Navigate to "Books" tab to view, add, edit, or delete books
4. Use "Search" tab for advanced filtering
5. Check "Analytics" tab for low stock monitoring

### **API Testing with Postman**
Import the following collection URL:
```
http://localhost:8080/api-docs
```

### **Keyboard Shortcuts**
- `Ctrl+N` / `Cmd+N`: Add new book
- `Ctrl+R` / `Cmd+R`: Refresh data
- `Escape`: Close modal dialogs
- `Enter`: Submit search forms

## 🛡 **Security Features**

- Input validation and sanitization
- SQL injection prevention through JPA
- XSS protection with proper HTML escaping
- CORS configuration for secure cross-origin requests
- Error handling without information disclosure

## 🐛 **Troubleshooting**

### **Common Issues**

1. **Connection Failed**
   - Ensure Spring Boot is running: `mvn spring-boot:run`
   - Check port 8080 is not in use: `netstat -an | grep 8080`
   - Access frontend via http://localhost:8080/ (not IDE server)

2. **Database Issues**
   - H2 console: http://localhost:8080/h2-console
   - Check JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`, Password: (empty)

3. **API Documentation**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs JSON: http://localhost:8080/api-docs

4. **CORS Errors**
   - Access frontend through Spring Boot (port 8080)
   - Check CorsConfig.java configuration
   - Avoid accessing from IDE development servers

## 🤝 **Contributing**

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 **Author**

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- Email: your.email@example.com

## 🙏 **Acknowledgments**

- Spring Boot team for the excellent framework
- H2 Database for providing an easy-to-use in-memory database
- Swagger/OpenAPI for comprehensive API documentation
- The open-source community for inspiration and best practices

---

## 📞 **Support**

If you encounter any issues or have questions:

1. Check the [troubleshooting section](#-troubleshooting)
2. Review the [API documentation](#-api-documentation)
3. Open an issue on GitHub
4. Contact the development team

**Happy coding! 🚀**