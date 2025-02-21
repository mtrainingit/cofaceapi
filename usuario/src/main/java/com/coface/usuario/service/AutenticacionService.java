package com.coface.usuario.service;

import com.coface.usuario.db.model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService {

    private final AuthenticationManager authenticationManager;

    public AutenticacionService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Usuario login(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return (Usuario) auth.getPrincipal();
    }
}
