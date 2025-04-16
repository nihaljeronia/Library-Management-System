package com.library.lms.service;

import com.library.lms.model.User;
import com.library.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Fetch all users from the database
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Fetch a user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Save a new user or update an existing user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Delete a user by ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Fetch users by status
    public List<User> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }

    // Update the status of a user
    public User updateUserStatus(Long id, String status) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(status);
            return userRepository.save(user);
        }
        return null;
    }

    public User login(String email, String password) {
        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with the provided email"));

        // Check if the password matches
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        // Return the authenticated user
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

}