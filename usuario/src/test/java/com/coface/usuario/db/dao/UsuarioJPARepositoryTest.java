package com.coface.usuario.db.dao;

import com.coface.usuario.db.model.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioJPARepositoryTest {

    @Autowired
    private UsuarioJPARepository underTest;

    @BeforeEach
    void setUp() {
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
        underTest.save(usuario1);
        underTest.save(usuario2);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void existsByEmailShouldBeTrue() {
        // given
        String email = "doe.john@example.com";

        // when
        boolean exists = underTest.existsByEmail(email);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmailShouldBeFalse() {
        // given
        String email = "doe.john1@example.com";

        // when
        boolean exists = underTest.existsByEmail(email);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void findByEmailShouldBeNotNull() {
        // given
        Usuario usuario3 = new Usuario(
                "Jane Doe",
                "doe.jane@example.com",
                "hashedPassword",
                2
        );
        usuario3 = underTest.save(usuario3);
        String email = "doe.jane@example.com";

        // when
        Optional<Usuario> actual = underTest.findByEmail(email);

        // then
        assertThat(actual).isEqualTo(Optional.of(usuario3));
    }

    @Test
    void findByEmailShouldBeNull() {
        // given
        Usuario usuario3 = new Usuario(
                "Jane Doe",
                "doe.jane@example.com",
                "hashedPassword",
                2
        );
        underTest.save(usuario3);
        String email = "doe.jane1@example.com";

        // when
        Optional<Usuario> actual = underTest.findByEmail(email);

        // then
        assertThat(actual).isEqualTo(Optional.empty());
    }

}