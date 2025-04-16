package com.library.lms.service;

import com.library.lms.model.Book;
import com.library.lms.model.BorrowRecord;
import com.library.lms.model.User;
import com.library.lms.repository.BookRepository;
import com.library.lms.repository.BorrowRecordRepository;
import com.library.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BorrowService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    // Method to borrow a book
    public String borrowBook(Long userId, String isbn) {
        // Check if the user exists and is APPROVED
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!"APPROVED".equals(user.getStatus())) {
            throw new RuntimeException("User is not approved to borrow books.");
        }

        // Check if the book exists and has available copies
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies of the book are available.");
        }

        // Reduce availableCopies by 1 and save the book
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Create and save a BorrowRecord
        BorrowRecord borrowRecord = new BorrowRecord(userId, isbn, new Date(), false);
        borrowRecordRepository.save(borrowRecord);

        return "Book borrowed successfully.";
    }

    // Method to return a book
    public String returnBook(Long id) {
        // Fetch the borrow record by its ID
        BorrowRecord record = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));

        // Check if the book has already been returned
        if (record.isReturned()) {
            throw new RuntimeException("Book has already been returned.");
        }

        // Mark the book as returned and set the return date
        record.setReturned(true);
        record.setReturnDate(new Date()); // Set the return date
        borrowRecordRepository.save(record);

        // Increment the available copies of the book
        Book book = bookRepository.findByIsbn(record.getIsbn())
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + record.getIsbn()));
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return "Book returned successfully!";
    }

    // Get all borrow records for a specific user
    public List<BorrowRecord> getBorrowRecordsByUser(Long userId) {
        return borrowRecordRepository.findByUserId(userId);
    }

    // Get all borrow records
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }
}