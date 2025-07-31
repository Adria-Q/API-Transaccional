package com.example.EjemploAPITransaccioal.Service;

import com.example.EjemploAPITransaccioal.Model.Usuario;
import com.example.EjemploAPITransaccioal.Repository.RepositoryUsuario;
import com.example.EjemploAPITransaccioal.config.JwtAuthenticationFilter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransaccionService {
    @Autowired
    private RepositoryUsuario repositoryUsuario;



    public String depositar(String cuenta,double monto){
        Usuario usuario = repositoryUsuario.findByNumeroCuenta(cuenta).orElseThrow();
        usuario.setSaldo(usuario.getSaldo()+ monto);
        repositoryUsuario.save(usuario);
        return "Deposito exitoso";


    }

     @Transactional
    public String retirar(String cuenta, double monto) {
        Usuario usuario = repositoryUsuario.findByNumeroCuenta(cuenta).orElseThrow();
        if (usuario.getSaldo() < monto) return "Saldo suficiente";
        usuario.setSaldo(usuario.getSaldo() - monto);
        repositoryUsuario.save(usuario);
        return "Retiro exitoso";


    }
    @Transactional
    public String transferir(String origen,String destino,Double monto){

    Usuario usuario = repositoryUsuario.findByNumeroCuenta(origen)
            .orElseThrow(() -> new RuntimeException("Cuenta origen no encontrada"));
    Usuario destinoUsuario = repositoryUsuario.findByNumeroCuenta(destino)
            .orElseThrow(() -> new RuntimeException("Cuenta destino no encontrada"));

    if (usuario.getSaldo() < monto) {
        return "Saldo insuficiente";
    }


    // Actualizar saldos
    usuario.setSaldo(usuario.getSaldo() - monto);
    destinoUsuario.setSaldo(destinoUsuario.getSaldo() + monto);

    repositoryUsuario.save(usuario);
    repositoryUsuario.save(destinoUsuario);

    return "Transferencia exitosa";
}


}
