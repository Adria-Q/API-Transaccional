package com.example.EjemploAPITransaccioal.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="Usuario")
// Clase entidad que representa al usuario del sistema.

public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;
    @NotBlank
    private String identificacion;

    @Email
    @NotBlank
    @Column(unique = true)
    private String correo;

   @NotBlank
   @Column(unique = true)
    private String contrasena;

    @NotBlank
    @Column(unique = true)
    private String numeroCuenta;

    @PositiveOrZero
    private double saldo;

    @AssertTrue
    private boolean habilitado;

    @PrePersist
    public void prePersist(){
        this.numeroCuenta= String.valueOf ((long) (Math.random()* 1_000_000_0000L));
         this.saldo=1_000_000.0;
    }


    // Implementa UserDetails para integrarse con Spring Security.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return habilitado;
    }


}
