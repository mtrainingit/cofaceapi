package com.coface.usuario.api.dto;

import com.coface.usuario.db.model.Direccion;
import com.coface.usuario.db.model.Tarea;

import java.util.List;

public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String email,
        List<String> rol,
        Direccion direccion,
        List<Tarea> tareas
) {
}
