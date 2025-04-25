package com.userApi.controller;

import com.userApi.dto.LoginResponseDTO;
import com.userApi.dto.UserRequestDTO;
import com.userApi.dto.UserResponseDTO;
import com.userApi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(summary = "Registra un nuevo usuario", description = "Crea usuario y retorna token JWT")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Errores de validación o token inválido")
    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> signUp(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        log.info("[sign-up] registrando email={}", userRequestDTO.getEmail());
        UserResponseDTO resp = userService.signUp(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);

    }
    @Operation(summary = "Login con JWT", description = "Refresca token existente")
    @ApiResponse(responseCode = "200", description = "Login exitoso",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.replaceFirst("^Bearer ", "");
        log.info("[login] autenticando con token=" + token.substring(0, 10) + "...");
        LoginResponseDTO resp = userService.login(token);
        return ResponseEntity.ok(resp);
    }
}
