package com.example.EjemploAPITransaccioal.Repository;

import com.example.EjemploAPITransaccioal.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository  // esta clase se encarga de acceder a la base de datos
public interface RepositoryUsuario extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByNumeroCuenta(String numeroCuenta);


}
