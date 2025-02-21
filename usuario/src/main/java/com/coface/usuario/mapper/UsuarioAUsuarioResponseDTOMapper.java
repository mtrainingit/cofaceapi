package com.coface.usuario.mapper;

import com.coface.usuario.db.model.Usuario;
import com.coface.usuario.api.dto.UsuarioResponseDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UsuarioAUsuarioResponseDTOMapper implements Function<Usuario, UsuarioResponseDTO> {

    @Override
    public UsuarioResponseDTO apply(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getAuthorities().stream().map(i -> i.getAuthority()).collect(Collectors.toList()),
                usuario.getDireccion(),
                usuario.getTareas()
        );
    }
}
