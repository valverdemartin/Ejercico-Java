package com.userApi.service;

import com.userApi.config.JwtUtil;
import com.userApi.dto.LoginResponseDTO;
import com.userApi.dto.PhoneDTO;
import com.userApi.dto.UserRequestDTO;
import com.userApi.dto.UserResponseDTO;
import com.userApi.entity.User;
import com.userApi.exception.BadRequestException;
import com.userApi.exception.UserAlreadyExistsException;
import com.userApi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

class UserServiceImplTest {

    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private UserServiceImpl userService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        jwtUtil = Mockito.mock(JwtUtil.class);
        userService = new UserServiceImpl(userRepository, jwtUtil);
    }

    @Test
    void testSignUpSuccess() {

        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("a2asfGfdf4");
        request.setName("Test User");
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber(1234567890L);
        phoneDTO.setCitycode(1);
        phoneDTO.setContrycode("001");
        request.setPhones(Collections.singletonList(phoneDTO));

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreated(java.time.LocalDateTime.now());
        user.setLastLogin(java.time.LocalDateTime.now());
        user.setIsActive(true);
        user.setToken("mockedToken");

        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        Mockito.when(jwtUtil.generateToken(request.getEmail())).thenReturn("mockedToken");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.signUp(request);

        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getCreated());
        Assertions.assertNotNull(response.getLastLogin());
        Assertions.assertTrue(response.getIsActive());
        Assertions.assertNotNull(response.getToken());
        Assertions.assertEquals("mockedToken", response.getToken());
    }

    @Test
    void testSignUpUserExists() {
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("a2asfGfdf4");

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        Exception exception = Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            userService.signUp(request);
        });
        Assertions.assertEquals("El usuario ya existe", exception.getMessage());
    }

    @Test
    void testLoginSuccess() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("a2asfGfdf4"));
        user.setCreated(java.time.LocalDateTime.now());
        user.setLastLogin(java.time.LocalDateTime.now());
        user.setIsActive(true);
        String token = "mockedToken";
        user.setToken(token);

        Mockito.when(jwtUtil.validateToken(token)).thenReturn(true);
        Mockito.when(jwtUtil.getSubject(token)).thenReturn("test@example.com");
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        Mockito.when(jwtUtil.generateToken("test@example.com")).thenReturn("newMockedToken");
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        LoginResponseDTO response = userService.login(token);

        Assertions.assertNotNull(response.getToken());
        Assertions.assertEquals("newMockedToken", response.getToken());
    }

    @Test
    void testLoginInvalidToken() {

        String invalidToken = "invalidToken";
        Mockito.when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            userService.login(invalidToken);
        });
        Assertions.assertEquals("Token inválido", exception.getMessage());
    }

    @Test
    void loginWithNonExistentUserThrowsBadRequestException() {
        String token = "mockedToken";
        Mockito.when(jwtUtil.getSubject(token)).thenReturn("nonexistent@example.com");
        Mockito.when(jwtUtil.validateToken(token)).thenReturn(true);
        Mockito.when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            userService.login(token);
        });
        Assertions.assertEquals("Usuario no encontrado", exception.getMessage());
    }


}
