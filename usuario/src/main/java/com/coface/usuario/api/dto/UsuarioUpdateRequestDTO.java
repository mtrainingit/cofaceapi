package com.coface.usuario.api.dto;

public record UsuarioUpdateRequestDTO(
        String nombre,
        String email
) {
}
