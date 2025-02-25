package com.coface.usuario.db.dao;

import com.coface.usuario.db.model.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioJDBCRepositoryTest {

    @Autowired
    private UsuarioJPARepository aux;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UsuarioJDBCRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new UsuarioJDBCRepository(
                jdbcTemplate,
                new DireccionJDBCRepository(jdbcTemplate),
                new TareaJDBCRepository(jdbcTemplate)
        );
        Usuario usuario1 = new Usuario(
                "John Doe",
                "doe.john@example.com",
                "hashedPassword",
                2
        );
        Usuario usuario2 = new Usuario(
                "Peter Smith",
                "smith.peter@example.com",
                "hashedPassword",
                2
        );
        aux.save(usuario1);
        aux.save(usuario2);
    }

    @AfterEach
    void tearDown() {
        aux.deleteAll();
    }

    @Test
    void existeUsuarioPorEmailShouldBeTrue() {
        // given
        String email = "doe.john@example.com";

        // when
        boolean exists = underTest.existeUsuarioPorEmail(email);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void existeUsuarioPorEmailShouldBeFalse() {
        // given
        String email = "doe.john1@example.com";

        // when
        boolean exists = underTest.existeUsuarioPorEmail(email);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void getUsuarioPorEmailShouldBeNotNull() {
        // given
        Usuario usuario3 = new Usuario(
                "Jane Doe",
                "doe.jane@example.com",
                "hashedPassword",
                2
        );
        usuario3 = underTest.saveUsuario(usuario3);
        String email = "doe.jane@example.com";

        // when
        Optional<Usuario> actual = underTest.getUsuarioPorEmail(email);

        // then
        assertThat(actual).isPresent().contains(usuario3);
    }

    @Test
    void getUsuarioPorEmailShouldBeNull() {
        // given
        Usuario usuario3 = new Usuario(
                "Jane Doe",
                "doe.jane@example.com",
                "hashedPassword",
                2
        );
        underTest.saveUsuario(usuario3);
        String email = "doe.jane1@example.com";

        // when
        Optional<Usuario> actual = underTest.getUsuarioPorEmail(email);

        // then
        assertThat(actual).isEqualTo(Optional.empty());
    }

}