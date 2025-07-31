package com.example.EjemploAPITransaccioal.Controller;

import com.example.EjemploAPITransaccioal.Exception.EmailAlreadyExistsException;
import com.example.EjemploAPITransaccioal.Exception.InvalidEmailException;
import com.example.EjemploAPITransaccioal.Exception.InvalidPasswordException;
import com.example.EjemploAPITransaccioal.Model.Usuario;
import com.example.EjemploAPITransaccioal.Service.UsuarioService;
import com.example.EjemploAPITransaccioal.config.JwtAuthenticationFilter;
import com.example.EjemploAPITransaccioal.dto.auth.ApiResponse;
import com.example.EjemploAPITransaccioal.dto.auth.LoginRequest;
import com.example.EjemploAPITransaccioal.dto.auth.LoginResponse;
import com.example.EjemploAPITransaccioal.dto.auth.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UsuarioService usuarioService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<Usuario>> registrar(@Valid @RequestBody RegisterRequest request) {
        try {
            ResponseEntity<ApiResponse<Usuario>> response = usuarioService.registrarUsuario(request);
            return response;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error en el servidor: " + e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Autenticar credenciales
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCorreo(),
                            request.getContrasena()
                    )
            );

            // Generar token JWT
            String token = jwtAuthenticationFilter.generarToken(request.getCorreo());

            return ResponseEntity.ok()
                    .body(new LoginResponse(true, "Autenticación exitosa", token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Correo o contraseña incorrectos", null));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new LoginResponse(false, "Usuario deshabilitado", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(false, "Error en el servidor: " + e.getMessage(), null));
        }
    }
}