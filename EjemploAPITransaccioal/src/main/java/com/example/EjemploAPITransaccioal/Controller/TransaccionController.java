package com.example.EjemploAPITransaccioal.Controller;

import com.example.EjemploAPITransaccioal.Model.Usuario;
import com.example.EjemploAPITransaccioal.Repository.RepositoryUsuario;
import com.example.EjemploAPITransaccioal.Service.TransaccionService;
import com.example.EjemploAPITransaccioal.config.JwtAuthenticationFilter;
import com.example.EjemploAPITransaccioal.dto.auth.TransferenciaRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transacciones")
@Transactional
public class TransaccionController {


    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @PostMapping("/depositar")
    public ResponseEntity<?> depositar(
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String token) {

        try {
            // Obtener usuario autenticado desde el token
            Usuario usuario = jwtAuthenticationFilter.obtenerUsuarioDelToken(token.replace("Bearer ", ""));
            String cuenta = body.get("cuenta");

            // Validar que la cuenta pertenece al usuario
            if (!usuario.getNumeroCuenta().equals(cuenta)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new TransaccionResponse("Número incorrecto, verifica tu número de cuenta"));
            }

            double monto = Double.parseDouble(body.get("monto"));
            String resultado = transaccionService.depositar(cuenta, monto);
            return ResponseEntity.ok(new TransaccionResponse(resultado));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new TransaccionResponse("Usuario no encontrado"));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TransaccionResponse("Monto inválido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TransaccionResponse("Error en el depósito: " + e.getMessage()));
        }
    }

    @PostMapping("/retirar")
    public ResponseEntity<?> retirar(
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String token) {

        try {
            // Obtener usuario autenticado desde el token
            Usuario usuario = jwtAuthenticationFilter.obtenerUsuarioDelToken(token.replace("Bearer ", ""));
            String cuenta = body.get("cuenta");

            // Validar que la cuenta pertenece al usuario
            if (!usuario.getNumeroCuenta().equals(cuenta)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new TransaccionResponse("Número incorrecto, verifica tu número de cuenta "));
            }

            double monto = Double.parseDouble(body.get("monto"));
            String resultado = transaccionService.retirar(cuenta, monto);
            return ResponseEntity.ok(new TransaccionResponse(resultado));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new TransaccionResponse("Usuario no encontrado"));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TransaccionResponse("Monto inválido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TransaccionResponse("Error en el retiro: " + e.getMessage()));
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(
            @RequestBody TransferenciaRequest request,
            @RequestHeader("Authorization") String token) {

        try {
            // Obtener usuario autenticado desde el token
            Usuario usuario = jwtAuthenticationFilter.obtenerUsuarioDelToken(token.replace("Bearer ", ""));

            // Validar que la cuenta origen pertenece al usuario
            if (!usuario.getNumeroCuenta().equals(request.getOrigen())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new TransaccionResponse("Número incorrecto, verifica tu número de cuenta"));
            }

            // Validarque no pueda retirar de otras  nuemro de cuentas cuentas
            if (request.getOrigen().equals(request.getDestino())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new TransaccionResponse("Número incorrecto, verifica tu número de cuenta"));
            }

            String resultado = transaccionService.transferir(
                    request.getOrigen(),
                    request.getDestino(),
                    request.getMonto()
            );
            return ResponseEntity.ok(new TransaccionResponse(resultado));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new TransaccionResponse("Cuenta no encontrada"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TransaccionResponse("Error en la transferencia: " + e.getMessage()));
        }
    }
}