package com.userApi.service;

import com.userApi.dto.LoginResponseDTO;
import com.userApi.dto.UserRequestDTO;
import com.userApi.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO signUp(UserRequestDTO userRequestDTO);
    LoginResponseDTO login(String token);
}
