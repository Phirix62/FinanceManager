package com.FinFlow.FinanceManager.controller;

import com.FinFlow.FinanceManager.dto.UserDTO;
import com.FinFlow.FinanceManager.entity.User;
import com.FinFlow.FinanceManager.services.user.UserService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for handling user-related operations such as registration and login.
 * Provides REST endpoints for user management.
 */
@RestController
@RequestMapping("api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Registers a new user with the provided user details.
     *
     * @param userDTO the user data transfer object containing registration details
     * @return a success message if registration is successful
     */
    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO) {
        logger.info("Registering user with username: {}", userDTO.getUsername());
        userService.registerUser(userDTO);
        logger.info("User registered successfully: {}", userDTO.getUsername());
        return "User registered successfully!";
    }

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param userDTO the user data transfer object containing login credentials
     * @return a ResponseEntity containing the authenticated user or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
        logger.info("Attempting login for user: {}", userDTO.getUsername());
        try {
            User user = userService.loginUser(userDTO.getUsername(), userDTO.getPassword());
            logger.info("Login successful for user: {}", userDTO.getUsername());
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            logger.warn("Login failed for user: {} - {}", userDTO.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during login for user: {}", userDTO.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }
}
