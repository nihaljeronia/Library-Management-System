package com.library.lms.service;

import com.library.lms.model.Book;
import com.library.lms.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    private final Map<String, Book> tempBooks = new HashMap<>();

    // Add a book to the temporary map
    public void addTempBook(Book book) {
        tempBooks.put(book.getIsbn(), book);
    }

    // View all books in the temporary map
    public Map<String, Book> viewAllTempBooks() {
        return new HashMap<>(tempBooks);
    }

    // Remove a book from the temporary map by ISBN
    public void removeTempBook(String isbn) {
        tempBooks.remove(isbn);
    }

    // Save all temporary books to the database
    public void saveAllTempBooks() {
        for (Book book : tempBooks.values()) {

            // Set availableCopies to totalCopies before saving
            book.setAvailableCopies(book.getTotalCopies());
            bookRepository.save(book); // Save the book to the database
        }
        tempBooks.clear(); // Clear the temporary books after saving
    }

    // Get all books from the database
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Search for books by title
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }


    // Update book details by ID with synchronization of available copies
    public void updateBookById(Long id, Book updatedBook) {
        Book existingBook = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));

        // Update the fields of the existing book
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setTotalCopies(updatedBook.getTotalCopies());
        existingBook.setAvailableCopies(updatedBook.getTotalCopies()); // Sync available copies

        bookRepository.save(existingBook); // Save the updated book
    }

    // Delete a book by ID
    public void deleteBookById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id); // Delete the book by ID
    }

    // Get only books with availableCopies > 0
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0);
    }

    // Fetch books by genre
    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenreIgnoreCase(genre);
    }

    // Fetch books by author
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorIgnoreCase(author);
    }

    // Fetch all books ordered by title
    public List<Book> getBooksOrderedByTitle() {
        return bookRepository.findAllByOrderByTitleAsc();
    }

    // Fetch all books ordered by available copies
    public List<Book> getBooksOrderedByAvailableCopies() {
        return bookRepository.findAllByOrderByAvailableCopiesDesc();
    }
}