package com.coface.usuario.service;

import com.coface.common.NotificacionCreateRequestDTO;
import com.coface.usuario.api.dto.UsuarioCreateRequestDTO;
import com.coface.usuario.exception.RecursoNoEncontradoException;
import com.coface.usuario.db.dao.UsuarioRepository;
import com.coface.usuario.db.model.Direccion;
import com.coface.usuario.db.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private Mono<Long> longMono;

    @Mock
    private KafkaTemplate<String, NotificacionCreateRequestDTO> kafkaTemplate;

    private UsuarioService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UsuarioService(usuarioRepository, passwordEncoder, webClientBuilder, kafkaTemplate);
    }

    @Test
    void getUsuarios() {
        // when
        underTest.getUsuarios();

        // then
        verify(usuarioRepository).getUsuarios();
    }

    @Test
    void getUsuarioPorId() {
        // given
        Long id = 3L;
        Usuario usuario = new Usuario(
                3L,
                "John Doe",
                "doe.john@example.com",
                "hashedPassword",
                2
        );
        usuario.setDireccion(new Direccion(
                3L,
                "Calle A 3",
                "33333",
                usuario
        ));
        when(usuarioRepository.getUsuarioPorId(id)).thenReturn(Optional.of(usuario));

        // when
        Usuario actual = underTest.getUsuarioPorId(id);

        // then
        assertEquals(usuario, actual);
    }

    @Test
    void getUsuarioPorIdShouldThrow() {
        // given
        Long id = 3L;
        when(usuarioRepository.getUsuarioPorId(id)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getUsuarioPorId(id))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("No se pudo encontrar un usuario con el id %s".formatted(id));
    }

    @Test
    void crearUsusario() {
        // given
        Long id = 3L;
        UsuarioCreateRequestDTO usuarioCreateRequestDTO = new UsuarioCreateRequestDTO(
                "John Doe",
                "doe.john@example.com",
                "password",
                "Calle A 3",
                "33333"
        );
        when(usuarioRepository.existeUsuarioPorEmail(usuarioCreateRequestDTO.email())).thenReturn(false);
        when(passwordEncoder.encode(usuarioCreateRequestDTO.password())).thenReturn("hashedPassword");
        Usuario usuarioInput = new Usuario(
                usuarioCreateRequestDTO.nombre(),
                usuarioCreateRequestDTO.email(),
                "hashedPassword",
                2
        );
        Usuario usuarioOutput = new Usuario(
                id,
                usuarioCreateRequestDTO.nombre(),
                usuarioCreateRequestDTO.email(),
                "hashedPassword",
                2
        );
        Usuario usuarioConDireccion = new Usuario(
                id,
                usuarioCreateRequestDTO.nombre(),
                usuarioCreateRequestDTO.email(),
                "hashedPassword",
                2
        );
        usuarioConDireccion.setDireccion(new Direccion(
                usuarioCreateRequestDTO.direccion(),
                usuarioCreateRequestDTO.codigoPostal(),
                usuarioConDireccion
        ));
        when(usuarioRepository.saveUsuario(usuarioInput)).thenReturn(usuarioOutput);
        when(usuarioRepository.saveUsuario(usuarioConDireccion)).thenReturn(usuarioConDireccion);

        /* when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Long.class)).thenReturn(Mono.just(id)); */

        when(kafkaTemplate.send(any(), any())).thenReturn(null);

        // when
        Long actual = underTest.crearUsusario(usuarioCreateRequestDTO);

        // then
        ArgumentCaptor<Usuario> argumentCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository, times(2)).saveUsuario(argumentCaptor.capture());
        List<Usuario> usuarios = argumentCaptor.getAllValues();
        assertEquals(usuarios.get(0).getNombre(), usuarioCreateRequestDTO.nombre());
        assertEquals(usuarios.get(1).getDireccion().getDireccion(), usuarioCreateRequestDTO.direccion());
        assertEquals(id, actual);
    }

    @Test
    void actualizarUsuario() {
    }

    @Test
    void eliminarUsuario() {
    }

    @Test
    void getUsuariosPaginados() {
    }

    @Test
    void asignarTarea() {
    }

    @Test
    void getTareasDeUsuario() {
    }

    @Test
    void getUsuariosReducidos() {
    }

    @Test
    void getUsuarioPorEmail() {
    }
}