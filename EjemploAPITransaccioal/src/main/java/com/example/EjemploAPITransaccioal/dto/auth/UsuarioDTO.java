package com.example.EjemploAPITransaccioal.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UsuarioDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String identificacion;
    private boolean habilitado;

}
