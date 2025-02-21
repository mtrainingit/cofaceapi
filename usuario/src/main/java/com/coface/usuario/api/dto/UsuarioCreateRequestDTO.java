package com.coface.usuario.api.dto;

public record UsuarioCreateRequestDTO(
        String nombre,
        String email,
        String password,
        String direccion,
        String codigoPostal
) {
}
