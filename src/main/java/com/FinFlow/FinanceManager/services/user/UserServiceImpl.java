package com.FinFlow.FinanceManager.services.user;

import com.FinFlow.FinanceManager.dto.UserDTO;
import com.FinFlow.FinanceManager.entity.User;
import com.FinFlow.FinanceManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service implementation for user-related operations such as registration and login.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Registers a new user with the provided user data.
     *
     * @param userDTO Data transfer object containing user registration information.
     * @return The saved User entity.
     */
    public User registerUser(UserDTO userDTO) {
        logger.info("Registering user with username: {}", userDTO.getUsername());
        // Check if username already exists
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            logger.warn("Username already exists: {}", userDTO.getUsername());
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with id: {}", savedUser.getId());
        return savedUser;
    }

    /**
     * Authenticates a user with the provided username and password.
     *
     * @param username The username of the user attempting to log in.
     * @param password The raw password to authenticate.
     * @return The authenticated User entity.
     * @throws RuntimeException if the user is not found or the password is invalid.
     */
    public User loginUser(String username, String password) {
        logger.info("Attempting login for username: {}", username);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                logger.warn("User not found for username: {}", username);
                return new RuntimeException("User not found");
            });

        // Compare encoded password
        if (passwordEncoder.matches(password, user.getPassword())) {
            logger.info("User login successful for username: {}", username);
            return user;
        } else {
            logger.warn("Invalid password attempt for username: {}", username);
            throw new RuntimeException("Invalid password");
        }
    }
}
