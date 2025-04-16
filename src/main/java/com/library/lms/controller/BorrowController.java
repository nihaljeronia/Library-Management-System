package com.library.lms.controller;

import com.library.lms.model.BorrowRecord;
import com.library.lms.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    // POST /borrow → Borrow a book
    @PostMapping
    public String borrowBook(@RequestBody Map<String, String> request) {
        Long userId = Long.parseLong(request.get("userId"));
        String isbn = request.get("isbn");
        return borrowService.borrowBook(userId, isbn);
    }

    // PUT /return/{recordId} → Return a borrowed book
    @PutMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        return borrowService.returnBook(id);
    }

    // GET /user/{userId} → Get all borrow records for a specific user
    @GetMapping("/user/{userId}")
    public List<BorrowRecord> getBorrowRecordsByUser(@PathVariable Long userId) {
        return borrowService.getBorrowRecordsByUser(userId);
    }
}