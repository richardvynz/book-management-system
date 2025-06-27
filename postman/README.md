# 📮 Postman Collection for Book Management API

## 📋 Overview
This directory contains Postman collection and environment files for comprehensive testing of the Book Management API.

## 📁 Files
- `Book-Management-API.postman_collection.json` - Complete API test collection with automated tests
- `Book-Management-Environment.postman_environment.json` - Environment variables and configuration
- `README.md` - This instruction file

## 🚀 Quick Start

### Prerequisites
1. **Postman installed** - Download from [postman.com](https://www.postman.com/downloads/)
2. **Application running** - Start your Spring Boot app: `mvn spring-boot:run`
3. **App accessible** - Ensure it's running on `http://localhost:8080`

### Import Steps

#### 1. Import Collection
1. Open Postman
2. Click **"Import"** button (top left)
3. Click **"Upload Files"**
4. Select `Book-Management-API.postman_collection.json`
5. Click **"Import"**

#### 2. Import Environment
1. Click the **Environment dropdown** (top right, near the eye icon)
2. Click **"Import"**
3. Select `Book-Management-Environment.postman_environment.json`
4. Click **"Import"**
5. Select **"Book Management Environment"** from the dropdown

### ✅ Verification
- You should see "Book Management API" collection in the left sidebar
- Environment should show "Book Management Environment" in top-right dropdown

## 🧪 Testing Workflow

### Recommended Test Order
1. **📚 Books CRUD Operations**
    - Create Book (stores ID automatically)
    - Get All Books
    - Get Book by ID
    - Update Book

2. **🔍 Search Operations**
    - Search by Author
    - Search by Title
    - Search by Keyword

3. **📊 Filter Operations**
    - Get Books by Year
    - Get Books by Price Range
    - Get Low Stock Books

4. **❌ Error Examples**
    - Validation Errors
    - Not Found Errors
    - Duplicate ISBN Errors

5. **🗑️ Cleanup**
    - Delete Created Book

### Running Tests

#### Option 1: Run Entire Collection
1. Right-click on **"Book Management API"** collection
2. Select **"Run collection"**
3. Click **"Run Book Management API"**
4. Watch automated tests execute

#### Option 2: Run Individual Requests
1. Click on any request in the collection
2. Click **"Send"** button
3. Check **"Test Results"** tab for automated test results

## 🔧 Environment Variables

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `baseUrl` | API base URL | `http://localhost:8080` |
| `bookId` | Created book ID | Auto-set by "Create Book" request |

### Customizing Environment
1. Click **gear icon** ⚙️ (top right)
2. Click on **"Book Management Environment"**
3. Modify values as needed
4. Click **"Update"**

## 📊 Test Coverage

### ✅ What's Tested
- **HTTP Status Codes**: 200, 201, 204, 400, 404, 409
- **Response Structure**: All fields and data types
- **CRUD Operations**: Create, Read, Update, Delete
- **Search & Filter**: All search endpoints
- **Error Handling**: Validation and business logic errors
- **Pagination**: Page structure and metadata

### 🔍 Automated Validations
- Status code verification
- Response body structure
- Data type validation
- Business logic verification
- Variable extraction and storage

## 🐛 Troubleshooting

### Common Issues

#### Collection Not Importing
- **Solution**: Ensure JSON file is valid and not corrupted
- **Check**: File should start with `{` and end with `}`

#### Environment Not Working
- **Solution**: Make sure environment is selected in dropdown
- **Check**: Variables should show `{{baseUrl}}` format in requests

#### Tests Failing
- **Solution**: Ensure Spring Boot app is running on port 8080
- **Check**: Visit `http://localhost:8080/swagger-ui.html` in browser

#### BookId Variable Empty
- **Solution**: Run "Create Book" request first
- **Check**: Look in environment variables after creating book

### Getting Help
1. Check console logs in Postman (View → Show Postman Console)
2. Verify app is running: `curl http://localhost:8080/api/v1/books`
3. Check application logs for errors

## 🎯 Best Practices

### Before Testing
1. ✅ Start fresh application instance
2. ✅ Clear H2 database (automatic on restart)
3. ✅ Select correct environment

### During Testing
1. 🔄 Run "Create Book" before dependent tests
2. 📝 Check test results after each request
3. 🧹 Run cleanup after testing

### After Testing
1. 🗑️ Delete test data (or restart app)
2. 📊 Review test results
3. 🐛 Report any issues found

## 📈 Advanced Usage

### Custom Test Scripts
Each request includes test scripts that:
- Validate response codes
- Check response structure
- Extract and store variables
- Log helpful information

### Extending Tests
To add more tests:
1. Duplicate existing request
2. Modify URL/body as needed
3. Update test scripts in "Tests" tab
4. Add to appropriate folder

## 🔗 Related Resources
- [API Documentation](http://localhost:8080/swagger-ui.html)
- [H2 Database Console](http://localhost:8080/h2-console)
- [Postman Documentation](https://learning.postman.com/docs/)

---
**Happy Testing! 🚀**