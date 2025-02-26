package com.coface.usuario;

import com.coface.usuario.api.dto.UsuarioCreateRequestDTO;
import com.coface.usuario.api.dto.UsuarioResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WebTestClient webTestClient;

    @AfterEach
    public void tearDown() {
        jdbcTemplate.update("DELETE FROM tareas");
        jdbcTemplate.update("DELETE FROM direcciones");
        jdbcTemplate.update("DELETE FROM usuarios");
        jdbcTemplate.update("alter sequence tareas_id_seq restart start with 1");
        jdbcTemplate.update("alter sequence direcciones_id_seq restart start with 1");
        jdbcTemplate.update("alter sequence usuarios_id_seq restart start with 1");
    }

    @Test
    void puedeCrearUsuario() {
        // given
        UsuarioCreateRequestDTO usuarioCreateRequestDTO = new UsuarioCreateRequestDTO(
                "John Doe",
                "doe.john@example.com",
                "password",
                "Calle A 3",
                "33333"
        );

        // when
        EntityExchangeResult<Long> entityExchangeResult = webTestClient.post()
                .uri("api/v1/usuario")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(usuarioCreateRequestDTO), UsuarioCreateRequestDTO.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CREATED)
                .expectBody(new ParameterizedTypeReference<Long>() {
                })
                .returnResult();
        Long id = entityExchangeResult.getResponseBody();
        // String token = entityExchangeResult.getResponseHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        List<UsuarioResponseDTO> usuarioResponseDTOs = webTestClient.get()
                .uri("api/v1/usuario")
                .accept(MediaType.APPLICATION_JSON)
                // .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<UsuarioResponseDTO>() {
                })
                .returnResult()
                .getResponseBody();

        UsuarioResponseDTO usuarioResponseDTO = webTestClient.get()
                .uri("api/v1/usuario/%s".formatted(id))
                .accept(MediaType.APPLICATION_JSON)
                // .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<UsuarioResponseDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(usuarioResponseDTOs)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("rol", "direccion", "tareas")
                .contains(new UsuarioResponseDTO(
                        id,
                        "John Doe",
                        "doe.john@example.com",
                        null,
                        null,
                        null
                ));

        assertThat(usuarioResponseDTO.id()).isEqualTo(id);
        assertThat(usuarioResponseDTO.rol()).isEqualTo(List.of("ROLE_USER"));
        assertThat(usuarioResponseDTO.direccion().getDireccion()).isEqualTo(usuarioCreateRequestDTO.direccion());
        assertThat(usuarioResponseDTO.direccion().getCodigoPostal()).isEqualTo(usuarioCreateRequestDTO.codigoPostal());
    }
}
