package com.FinFlow.FinanceManager.controller;

import com.FinFlow.FinanceManager.dto.UserDTO;
import com.FinFlow.FinanceManager.entity.User;
import com.FinFlow.FinanceManager.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldReturnOkWithUser_WhenRegistrationIsSuccessful() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        User user = new User();
        when(userService.registerUser(userDTO)).thenReturn(user);

        ResponseEntity<?> result = userController.registerUser(userDTO);

        verify(userService, times(1)).registerUser(userDTO);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(user, result.getBody());
    }

    @Test
    void registerUser_ShouldReturnBadRequest_WhenIllegalArgumentException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("baduser");
        when(userService.registerUser(userDTO)).thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<?> result = userController.registerUser(userDTO);

        verify(userService, times(1)).registerUser(userDTO);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid data", result.getBody());
    }

    @Test
    void registerUser_ShouldReturnInternalServerError_WhenOtherException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("erroruser");
        when(userService.registerUser(userDTO)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> result = userController.registerUser(userDTO);

        verify(userService, times(1)).registerUser(userDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Registration failed", result.getBody());
    }

    @Test
    void loginUser_ShouldReturnOkWithUser_WhenCredentialsAreValid() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("testpass");
        User user = new User();
        when(userService.loginUser("testuser", "testpass")).thenReturn(user);

        ResponseEntity<?> response = userController.loginUser(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void loginUser_ShouldReturnUnauthorized_WhenEntityNotFoundException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("notfound");
        userDTO.setPassword("wrong");
        when(userService.loginUser("notfound", "wrong"))
                .thenThrow(new EntityNotFoundException("User not found"));

        ResponseEntity<?> response = userController.loginUser(userDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void loginUser_ShouldReturnUnauthorized_WhenIllegalArgumentException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("baduser");
        userDTO.setPassword("badpass");
        when(userService.loginUser("baduser", "badpass"))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        ResponseEntity<?> response = userController.loginUser(userDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void loginUser_ShouldReturnInternalServerError_WhenOtherException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("error");
        userDTO.setPassword("error");
        when(userService.loginUser("error", "error"))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = userController.loginUser(userDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Login failed", response.getBody());
    }
}
