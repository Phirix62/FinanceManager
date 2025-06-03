package com.FinFlow.FinanceManager.service;

import com.FinFlow.FinanceManager.dto.UserDTO;
import com.FinFlow.FinanceManager.entity.User;
import com.FinFlow.FinanceManager.repository.UserRepository;
import com.FinFlow.FinanceManager.services.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




class UserServiceImplTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl();
        passwordEncoder = new BCryptPasswordEncoder();

        // Inject mock repository
        java.lang.reflect.Field repoField;
        try {
            repoField = UserServiceImpl.class.getDeclaredField("userRepository");
            repoField.setAccessible(true);
            repoField.set(userService, userRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRegisterUser_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");

        User savedUser = new User();
        savedUser.setUsername("testuser");
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(userDTO);

        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertNotNull(result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_PasswordIsEncoded() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("user");
        userDTO.setEmail("user@email.com");
        userDTO.setPassword("plainPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerUser(userDTO);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertNotEquals("plainPassword", savedUser.getPassword());
        assertTrue(passwordEncoder.matches("plainPassword", savedUser.getPassword()));
    }

    @Test
    void testLoginUser_Success() {
        String username = "testuser";
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Inject the same encoder as in the service
        java.lang.reflect.Field encoderField;
        try {
            encoderField = UserServiceImpl.class.getDeclaredField("passwordEncoder");
            encoderField.setAccessible(true);
            encoderField.set(userService, passwordEncoder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        User result = userService.loginUser(username, rawPassword);

        assertEquals(username, result.getUsername());
    }

    @Test
    void testLoginUser_UserNotFound() {
        when(userRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.loginUser("nouser", "anyPassword");
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLoginUser_InvalidPassword() {
        String username = "testuser";
        String correctPassword = "correct";
        String wrongPassword = "wrong";
        String encodedPassword = passwordEncoder.encode(correctPassword);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Inject the same encoder as in the service
        java.lang.reflect.Field encoderField;
        try {
            encoderField = UserServiceImpl.class.getDeclaredField("passwordEncoder");
            encoderField.setAccessible(true);
            encoderField.set(userService, passwordEncoder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.loginUser(username, wrongPassword);
        });

        assertEquals("Invalid password", exception.getMessage());
    }
}
