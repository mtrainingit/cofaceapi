package com.coface.usuario.config.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ComponentesDeSeguridadConfig {

    @Bean
    @Primary
    @Qualifier("bcrypt")
    public PasswordEncoder getPasswordBCryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Qualifier("md4")
    public PasswordEncoder getPasswordEncoder() {
        return new Md4PasswordEncoder();
    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider(
            @Qualifier("detail-usuario") UserDetailsService userDetailsService,
            @Qualifier("bcrypt") PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(
            AuthenticationConfiguration authentication
    ) throws Exception {
        return authentication.getAuthenticationManager();
    }
}
