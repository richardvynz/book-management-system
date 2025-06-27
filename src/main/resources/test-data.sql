-- Test data for integration tests
INSERT INTO books (id, title, author, isbn, published_year, description, price, stock_quantity, created_at, updated_at)
VALUES
    (1, 'Test Book 1', 'Test Author 1', '978-0-123456-78-9', 2020, 'Test description 1', 29.99, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Test Book 2', 'Test Author 2', '978-0-123456-79-6', 2021, 'Test description 2', 39.99, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'Low Stock Book', 'Test Author 3', '978-0-123456-80-2', 2022, 'Test description 3', 19.99, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
