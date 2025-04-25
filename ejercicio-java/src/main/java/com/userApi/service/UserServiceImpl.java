package com.userApi.service;

import com.userApi.config.JwtUtil;
import com.userApi.dto.LoginResponseDTO;
import com.userApi.dto.UserRequestDTO;
import com.userApi.dto.UserResponseDTO;
import com.userApi.entity.User;
import com.userApi.exception.BadRequestException;
import com.userApi.exception.UserAlreadyExistsException;
import com.userApi.mapper.UserMapper;
import com.userApi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional  // marca toda la clase como transaccional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponseDTO signUp(UserRequestDTO userRequestDTO) {

        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El usuario ya existe");
        }

        User user = UserMapper.toEntity(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        String token = jwtUtil.generateToken(user.getEmail());
        user.setToken(token);

        User savedUser = userRepository.save(user);
        log.info("Usuario creado id={} email={}", user.getId(), user.getEmail());
        return UserMapper.toResponse(savedUser);
    }

    @Override
    public LoginResponseDTO login(String token) {
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new BadRequestException("Token inválido");
        }
        String email = jwtUtil.getSubject(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        if (!token.equals(user.getToken())) {
            throw new BadRequestException("Token inválido");
        }
        String newToken = jwtUtil.generateToken(email);
        user.setToken(newToken);
        user.setLastLogin(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("[login] usuario {} autenticado", email);
        return UserMapper.toLoginResponse(updatedUser);
    }
}
