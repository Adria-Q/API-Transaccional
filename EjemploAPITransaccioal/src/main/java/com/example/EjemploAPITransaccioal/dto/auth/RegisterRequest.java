package com.example.EjemploAPITransaccioal.dto.auth;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest{

    private String nombres;
    private String apellidos;
    private String identificacion;
    private String correo;
    private String contrasena;
    private boolean habilitado;


}


