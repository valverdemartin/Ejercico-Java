package com.userApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequestDTO {

    private String name;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Formato de correo inválido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(
            regexp = "^(?=(?:[^A-Z]*[A-Z]){1})(?=(?:\\D*\\d\\D*\\d\\D*$))[a-z\\dA-Z]{8,12}$",
            message = "Formato de contraseña inválido"
    )
    private String password;
    private List<PhoneDTO> phones;

}
