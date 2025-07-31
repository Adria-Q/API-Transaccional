package com.example.EjemploAPITransaccioal.Service;

import com.example.EjemploAPITransaccioal.Exception.EmailAlreadyExistsException;
import com.example.EjemploAPITransaccioal.Exception.InvalidEmailException;
import com.example.EjemploAPITransaccioal.Exception.InvalidPasswordException;
import com.example.EjemploAPITransaccioal.Model.Usuario;
import com.example.EjemploAPITransaccioal.Repository.RepositoryUsuario;
import com.example.EjemploAPITransaccioal.dto.auth.ApiResponse;
import com.example.EjemploAPITransaccioal.dto.auth.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Clase interna para respuestas JSON
    private static class UserResponse {
        private boolean success;
        private String message;
        private Usuario data;

        // Constructores, getters y setters
        public UserResponse(boolean success, String message, Usuario data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // Getters necesarios para la serialización JSON
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Usuario getData() { return data; }
    }

    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        return repositoryUsuario.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public ResponseEntity<ApiResponse<Usuario>> registrarUsuario(RegisterRequest request) {
        try {
            // Validar correo único
            if (repositoryUsuario.findByCorreo(request.getCorreo()).isPresent()) {
                throw new EmailAlreadyExistsException("Ya existe un usuario con ese correo");
            }

            // Validar formato de correo
            if (!request.getCorreo().contains("@")) {
                throw new InvalidEmailException("Correo electrónico inválido");
            }

            // Validar contraseña
            if (request.getContrasena() == null || !request.getContrasena().matches("^(?=.*[a-zA-Z])(?=.*\\d).+$")) {
                throw new InvalidPasswordException("La contraseña debe contener letras y números");
            }

            Usuario usuario = new Usuario();
            usuario.setNombres(request.getNombres());
            usuario.setApellidos(request.getApellidos());
            usuario.setCorreo(request.getCorreo());
            usuario.setIdentificacion(request.getIdentificacion());
            usuario.setHabilitado(request.isHabilitado());
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));

            Usuario usuarioGuardado = repositoryUsuario.save(usuario);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Usuario registrado exitosamente",
                            usuarioGuardado
                    )
            );

        } catch (EmailAlreadyExistsException | InvalidEmailException | InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al registrar usuario: " + e.getMessage(), null));
        }
    }

    public ResponseEntity<ApiResponse<Usuario>> autenticarUsuario(String correo, String contrasenaIngresada) {
        try {
            Optional<Usuario> usuario = repositoryUsuario.findByCorreo(correo);

            if (usuario.isPresent()) {
                Usuario u = usuario.get();
                if (passwordEncoder.matches(contrasenaIngresada, u.getContrasena()) && u.isHabilitado()) {
                    return ResponseEntity.ok(
                            new ApiResponse<>(
                                    true,
                                    "Autenticación exitosa",
                                    u
                            )
                    );
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Credenciales inválidas", null));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Usuario no encontrado", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error en la autenticación: " + e.getMessage(), null));
        }
    }

    public ResponseEntity<ApiResponse<Usuario>> obtenerPorCuenta(String cuenta) {
        try {
            Optional<Usuario> usuario = repositoryUsuario.findByNumeroCuenta(cuenta);

            if (usuario.isPresent()) {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Usuario encontrado",
                                usuario.get()
                        )
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "No se encontró usuario con esa cuenta", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al buscar usuario: " + e.getMessage(), null));
        }
    }
}