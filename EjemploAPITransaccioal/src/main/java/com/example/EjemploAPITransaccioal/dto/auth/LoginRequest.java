package com.example.EjemploAPITransaccioal.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginRequest {
    @Email
    private String correo;
    @NotBlank
    private String contrasena;


}
