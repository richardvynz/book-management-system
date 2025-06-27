package com.richardvinz.Book_Management_App.repository;

import com.richardvinz.Book_Management_App.entity.Book;
import com.richardvinz.Book_Management_App.testUtil.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Book Repository Tests")
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook1;
    private Book testBook2;
    private Book lowStockBook;

    @BeforeEach
    void setUp() {
        testBook1 = TestDataBuilder.createBook(null, "The Great Gatsby", "F. Scott Fitzgerald", "978-0-123456-78-9");
        testBook1.setPrice(29.99);
        testBook1.setStockQuantity(100);
        testBook1.setPublishedYear(1925);

        testBook2 = TestDataBuilder.createBook(null, "To Kill a Mockingbird", "Harper Lee", "978-0-123456-79-6");
        testBook2.setPrice(25.99);
        testBook2.setStockQuantity(75);
        testBook2.setPublishedYear(1960);

        lowStockBook = TestDataBuilder.createBook(null, "1984", "George Orwell", "978-0-123456-80-2");
        lowStockBook.setPrice(19.99);
        lowStockBook.setStockQuantity(5);
        lowStockBook.setPublishedYear(1949);

        entityManager.persistAndFlush(testBook1);
        entityManager.persistAndFlush(testBook2);
        entityManager.persistAndFlush(lowStockBook);
    }

    @Test
    @DisplayName("Should find book by ISBN")
    void shouldFindBookByIsbn() {
        // When
        Optional<Book> result = bookRepository.findByIsbn("978-0-123456-78-9");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("The Great Gatsby");
        assertThat(result.get().getAuthor()).isEqualTo("F. Scott Fitzgerald");
    }

    @Test
    @DisplayName("Should return empty when ISBN not found")
    void shouldReturnEmptyWhenIsbnNotFound() {
        // When
        Optional<Book> result = bookRepository.findByIsbn("978-0-999999-99-9");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should find books by author containing ignore case")
    void shouldFindBooksByAuthorContainingIgnoreCase() {
        // When
        List<Book> result = bookRepository.findByAuthorContainingIgnoreCase("fitzgerald");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).isEqualTo("F. Scott Fitzgerald");
    }

    @Test
    @DisplayName("Should find books by title containing ignore case")
    void shouldFindBooksByTitleContainingIgnoreCase() {
        // When
        List<Book> result = bookRepository.findByTitleContainingIgnoreCase("great");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("The Great Gatsby");
    }

    @Test
    @DisplayName("Should find books by keyword")
    void shouldFindBooksByKeyword() {
        // When
        Page<Book> result = bookRepository.findByKeyword("great", PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("The Great Gatsby");
    }

    @Test
    @DisplayName("Should find books by published year")
    void shouldFindBooksByPublishedYear() {
        // When
        List<Book> result = bookRepository.findByPublishedYear(1925);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("The Great Gatsby");
    }

    @Test
    @DisplayName("Should find books by price range")
    void shouldFindBooksByPriceRange() {
        // When
        List<Book> result = bookRepository.findByPriceBetween(20.0, 30.0);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("The Great Gatsby", "To Kill a Mockingbird");
    }

    @Test
    @DisplayName("Should find books with stock quantity less than threshold")
    void shouldFindBooksWithStockQuantityLessThanThreshold() {
        // When
        List<Book> result = bookRepository.findByStockQuantityLessThan(10);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("1984");
        assertThat(result.get(0).getStockQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should check if book exists by ISBN")
    void shouldCheckIfBookExistsByIsbn() {
        // When
        boolean exists = bookRepository.existsByIsbn("978-0-123456-78-9");
        boolean notExists = bookRepository.existsByIsbn("978-0-999999-99-9");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}