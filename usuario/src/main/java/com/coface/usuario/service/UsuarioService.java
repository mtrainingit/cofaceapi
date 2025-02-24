package com.coface.usuario.service;

import com.coface.usuario.api.dto.NotificacionCreateRequestDTO;
import com.coface.usuario.api.dto.UsuarioCreateRequestDTO;
import com.coface.usuario.api.dto.UsuarioUpdateRequestDTO;
import com.coface.usuario.db.dao.UsuarioRepository;
import com.coface.usuario.db.model.Direccion;
import com.coface.usuario.db.model.Tarea;
import com.coface.usuario.db.model.Usuario;
import com.coface.usuario.db.model.UsuarioReducidoDTO;
import com.coface.usuario.exception.ConflictoCampoUnicoException;
import com.coface.usuario.exception.RecursoNoEncontradoException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final WebClient.Builder webClientBuilder;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public UsuarioService(
            @Qualifier("usuario-jdbc") UsuarioRepository usuarioRepository,
            @Qualifier("bcrypt") PasswordEncoder passwordEncoder,
            WebClient.Builder webClientBuilder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.webClientBuilder = webClientBuilder;
    }

    public List<Usuario> getUsuarios() {
        return usuarioRepository.getUsuarios();
    }

    public Usuario getUsuarioPorId(Long id) {
        return usuarioRepository.getUsuarioPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("No se pudo encontrar un usuario con el id " + id));
    }

    @Transactional
    public Long crearUsusario(UsuarioCreateRequestDTO usuarioCreateRequestDTO) {
        if (usuarioRepository.existeUsuarioPorEmail(usuarioCreateRequestDTO.email())) {
            throw new ConflictoCampoUnicoException("El email " + usuarioCreateRequestDTO.email() + " ya existe");
        }
        String password = passwordEncoder.encode(usuarioCreateRequestDTO.password());
        Usuario usuario = usuarioRepository.saveUsuario(new Usuario(
                usuarioCreateRequestDTO.nombre(),
                usuarioCreateRequestDTO.email(),
                password,
                2
        ));
        usuario.setDireccion(new Direccion(
                usuarioCreateRequestDTO.direccion(),
                usuarioCreateRequestDTO.codigoPostal(),
                usuario
        ));
        usuarioRepository.saveUsuario(usuario);
        webClientBuilder.baseUrl("http://localhost:8081").build().post()
                .uri("/api/v1/notificacion")
                .bodyValue(new NotificacionCreateRequestDTO(
                        usuario.getId(),
                        usuario.getUsername()
                ))
                .retrieve()
                .bodyToMono(Long.class)
                .doOnNext(id -> logger.info("Notificacion creada para usuario con id {}", id))
                .doOnError(error -> {
                    throw new RuntimeException(error.getMessage());
                })
                .subscribe();
        return usuario.getId();
    }

    @Transactional
    public Long actualizarUsuario(Long id, UsuarioUpdateRequestDTO usuarioUpdateRequestDTO) {
        Usuario usuario = usuarioRepository.getUsuarioPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("No se pudo encontrar un usuario con id " + id));
        if (usuarioUpdateRequestDTO.nombre() != null && !usuarioUpdateRequestDTO.nombre().isEmpty()) {
            usuario.setNombre(usuarioUpdateRequestDTO.nombre());
        }
        if (usuarioUpdateRequestDTO.email() != null && !usuarioUpdateRequestDTO.email().isEmpty()) {
            if (usuarioRepository.existeUsuarioPorEmail(usuarioUpdateRequestDTO.email())) {
                throw new ConflictoCampoUnicoException("El email " + usuarioUpdateRequestDTO.email() + " ya existe");
            }
            usuario.setEmail(usuarioUpdateRequestDTO.email());
        }
        return usuarioRepository.saveUsuario(usuario).getId();
    }

    @Transactional
    public Long eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.getUsuarioPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("No se pudo encontrar un usuario con id " + id));
        return usuarioRepository.deleteUsuario(id);
    }

    public Page<Usuario> getUsuariosPaginados(int pagina, int tamano, String ordPor, String dirOrd) {
        return usuarioRepository.getUsuariosPaginados(pagina, tamano, ordPor, dirOrd);
    }

    @Transactional
    public Long asignarTarea(Long id, Tarea tarea) {
        Usuario usuario = usuarioRepository.getUsuarioPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("No se pudo encontrar un usuario con id " + id));
        usuario.setTareas(usuarioRepository.encontrarTareasPorUsuario(usuario));
        tarea.setUsuario(usuario);
        usuario.asignaTarea(tarea);
        return usuarioRepository.saveUsuario(usuario).getId();
    }

    @Transactional
    public Usuario getTareasDeUsuario(Long id) {
        Usuario usuario = usuarioRepository.getUsuarioPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("No se pudo encontrar un usuario con id " + id));
        usuario.setTareas(usuarioRepository.encontrarTareasPorUsuario(usuario));
        return usuario;
    }

    public List<UsuarioReducidoDTO> getUsuariosReducidos() {
        return usuarioRepository.getUsuariosReducidos();
    }

    public Usuario getUsuarioPorEmail(String email) {
        return usuarioRepository.getUsuarioPorEmail(email).orElseThrow(() -> new RecursoNoEncontradoException("No se pudo encontrar un usuario con el email " + email));
    }
}
