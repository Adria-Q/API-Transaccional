package com.example.EjemploAPITransaccioal.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferenciaRequest {
        private String origen;
        private String destino;
        private double monto;

    }

