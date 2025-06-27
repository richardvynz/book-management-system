## 📮 API Testing with Postman

### Quick Setup
1. **Import Collection & Environment**
   ```bash
   # Files located in /postman directory
   - Book-Management-API.postman_collection.json
   - Book-Management-Environment.postman_environment.json
   ```

2. **Import in Postman**
    - Open Postman → Import → Upload Files
    - Select both files from `/postman` directory
    - Choose "Book Management Environment" from dropdown

3. **Start Testing**
   ```bash
   mvn spring-boot:run
   # Then run Postman collection
   ```

### 🎯 What's Included
- ✅ **Complete CRUD Operations** with automated tests
- ✅ **Search & Filter Endpoints** with validation
- ✅ **Error Handling Examples** (400, 404, 409 responses)
- ✅ **Automated Variable Management** (stores created book ID)
- ✅ **Full Test Coverage** with assertions

### 📊 Test Results
Each request includes automated tests that verify:
- HTTP status codes
- Response structure and data types
- Business logic correctness
- Error handling behavior

**📁 Detailed instructions available in `/postman/README.md`**