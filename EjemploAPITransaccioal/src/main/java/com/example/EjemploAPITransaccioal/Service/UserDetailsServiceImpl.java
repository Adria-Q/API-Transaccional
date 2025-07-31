package com.example.EjemploAPITransaccioal.Service;

import com.example.EjemploAPITransaccioal.Repository.RepositoryUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException{
        return repositoryUsuario.findByCorreo(correo)
                .orElseThrow(()->new UsernameNotFoundException("correo no resgistrado:" + correo));

    }
}
