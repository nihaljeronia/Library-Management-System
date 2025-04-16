package com.library.lms.controller;

import com.library.lms.model.Book;
import com.library.lms.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AdminDashboardController {


    @Autowired
    private LibraryService libraryService;

    // GET /dashboard/admin/books/temp → Return all tempBooks from LibraryService
    @GetMapping("/dashboard/admin/books/temp") // ------------------------------------------------//
    public Map<String, Book> getTempBooks() {
        return libraryService.viewAllTempBooks();
    }

    // POST /books/save → Save all temporary books to the database
    @PostMapping("/books/save") // -------------------------------------------------------------------//
    public ResponseEntity<String> saveAllBooks() {
        libraryService.saveAllTempBooks();
        return ResponseEntity.ok("All temporary books saved successfully.");
    }

    // PUT /books/update/{id} → Update book details in the database
    @PutMapping("/books/update/{id}") // --------------------------------------------------------------//
    public String updateBook(@PathVariable Long id, @RequestBody Book book) {
        libraryService.updateBookById(id, book);
        return "Book details updated successfully.";
    }

}