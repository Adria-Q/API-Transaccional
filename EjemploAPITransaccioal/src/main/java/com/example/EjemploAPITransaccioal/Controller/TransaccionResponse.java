package com.example.EjemploAPITransaccioal.Controller;

public class TransaccionResponse {
    private String mensaje;

    public TransaccionResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
