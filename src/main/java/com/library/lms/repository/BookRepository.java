package com.library.lms.repository;

import com.library.lms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAvailableCopiesGreaterThan(int availableCopies);
    Optional<Book> findByIsbn(String isbn);

    // Find books by genre (case-insensitive)
    List<Book> findByGenreIgnoreCase(String genre);

    // Find books by author (case-insensitive)
    List<Book> findByAuthorIgnoreCase(String author);

    // Find all books ordered by title in ascending order
    List<Book> findAllByOrderByTitleAsc();

    // Find all books ordered by available copies in descending order
    List<Book> findAllByOrderByAvailableCopiesDesc();
}