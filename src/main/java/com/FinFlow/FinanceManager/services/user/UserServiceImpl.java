package com.FinFlow.FinanceManager.services.user;

import com.FinFlow.FinanceManager.dto.UserDTO;
import com.FinFlow.FinanceManager.entity.User;
import com.FinFlow.FinanceManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    public User loginUser(String username, String password) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    // Compare encoded password
    if (passwordEncoder.matches(password, user.getPassword())) {
        return user;
    } else {
        throw new RuntimeException("Invalid password");
    }
}

}
