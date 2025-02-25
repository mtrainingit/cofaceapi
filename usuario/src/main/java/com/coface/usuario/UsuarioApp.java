package com.coface.usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.coface.usuario")
public class UsuarioApp {
    public static void main( String[] args ) {
        SpringApplication.run(UsuarioApp.class, args);
    }
}
