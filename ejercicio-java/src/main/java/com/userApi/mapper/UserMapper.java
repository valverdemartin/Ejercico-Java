package com.userApi.mapper;

import com.userApi.dto.LoginResponseDTO;
import com.userApi.dto.UserRequestDTO;
import com.userApi.dto.UserResponseDTO;
import com.userApi.entity.Phone;
import com.userApi.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toEntity(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setLastLogin(now);
        user.setIsActive(true);

        if (userRequestDTO.getPhones() != null) {
            List<Phone> phones = userRequestDTO.getPhones().stream()
                    .map(phoneDTO -> PhoneMapper.toEntity(phoneDTO, user))
                    .collect(Collectors.toList());
            user.setPhones(phones);
        }

        return user;
    }

    public static UserResponseDTO toResponse(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setCreated(user.getCreated());
        response.setLastLogin(user.getLastLogin());
        response.setToken(user.getToken());
        response.setIsActive(user.getIsActive());
        return response;
    }

    public static LoginResponseDTO toLoginResponse(User user) {
        if (user == null) {
            return null;
        }
        return new LoginResponseDTO(
                user.getId(),
                user.getCreated(),
                user.getLastLogin(),
                user.getToken(),
                user.getIsActive(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                PhoneMapper.toPhoneDTOList(user.getPhones())
        );
    }
}