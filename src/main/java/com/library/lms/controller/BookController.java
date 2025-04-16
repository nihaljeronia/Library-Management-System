package com.library.lms.controller;

import com.library.lms.model.Book;
import com.library.lms.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private LibraryService libraryService;

    // Add a book to the temporary map
    @PostMapping("/temp")//--------------------------------------//
    public String addTempBook(@RequestBody Book book) {
        libraryService.addTempBook(book);
        return "Book added to temporary storage.";
    }

    // Remove a book from the temporary map by ISBN
    @DeleteMapping("/temp/{isbn}")//-------------------------------------//
    public String removeTempBook(@PathVariable String isbn) {
        libraryService.removeTempBook(isbn);
        return "Book removed from temporary storage.";
    }

    // 5. GET /all → Return all books from the database
    @GetMapping("/all")//---------------------------------------------//
    public List<Book> getAllBooks() {
        return libraryService.getAllBooks();
    }

    // 6. GET /available → Return only books with availableCopies > 0
    @GetMapping("/available")
    public List<Book> getAvailableBooks() {
        return libraryService.getAvailableBooks();
    }

    // 9. DELETE /delete/{id} → Delete a book from the database
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        libraryService.deleteBookById(id);
        return ResponseEntity.ok("Book deleted successfully.");
    }

}