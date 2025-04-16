package com.library.lms.controller;

import com.library.lms.model.BorrowRecord;
import com.library.lms.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard/user")
public class UserDashboardController {

    @Autowired
    private BorrowService borrowService;

    // GET /dashboard/user/{id}/books → Return borrowed books (not returned yet)
    @GetMapping("/{id}/books")
    public List<BorrowRecord> getBorrowedBooks(@PathVariable Long id) {
        return borrowService.getBorrowRecordsByUser(id).stream()
                .filter(record -> !record.isReturned()) // Filter records where returned = false
                .toList();
    }

    // GET /dashboard/user/{id}/history → Return borrow history
    @GetMapping("/{id}/history")
    public List<BorrowRecord> getBorrowHistory(@PathVariable Long id) {
        return borrowService.getBorrowRecordsByUser(id); // Return all borrow records for the user
    }
}