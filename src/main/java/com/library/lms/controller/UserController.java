package com.library.lms.controller;

import com.library.lms.model.User;
import com.library.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/dashboard/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Create a new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // Check if the email already exists
            if (userService.getUserByEmail(user.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Error: User with this email already exists.");
            }

            // Set default status to "PENDING"
            user.setStatus("PENDING");

            // Save the user
            User savedUser = userService.saveUser(user);

            // Return success response
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Unable to register user. Please try again later.");
        }
    }

    // Approve a user
    @PutMapping("/approve/{id}")
    public User approveUser(@PathVariable Long id) {
        return userService.updateUserStatus(id, "APPROVED");
    }

    // DELETE /dashboard/admin/users/reject/{id} → Reject and delete a user
    @DeleteMapping("/reject/{id}")
    public String rejectUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User rejected and removed from the database.";
    }

    @PutMapping("/snooze/{id}")
    public String snoozeUser(@PathVariable Long id) {
        userService.updateUserStatus(id, "PENDING");
        return "User snoozed Succesfully";
    }

    // DELETE /dashboard/admin/users/delete/{id} → Delete a user
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if ("APPROVED".equals(user.getStatus())) {
            userService.deleteUser(id);
            return "User deleted successfully.";
        }
        return "Only approved users can be deleted.";
    }
}