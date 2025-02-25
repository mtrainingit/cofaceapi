package com.coface.usuario.db.dao;

import com.coface.usuario.db.model.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class UsuarioJPARepositoryAdapterTest {

    private AutoCloseable closeable;

    @Mock private UsuarioJPARepository aux;

    private UsuarioJPARepositoryAdapter underTest;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        underTest = new UsuarioJPARepositoryAdapter(aux);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void existeUsuarioPorEmailShouldCall() {
        // given
        String email = "doe.john@example.com";

        // when
        boolean exists = underTest.existeUsuarioPorEmail(email);

        // then
        verify(aux).existsByEmail(email);
    }

    @Test
    void getUsuarioPorEmailShouldCall() {
        // given
        String email = "doe.jane@example.com";

        // when
        Optional<Usuario> actual = underTest.getUsuarioPorEmail(email);

        // then
        verify(aux).findByEmail(email);
    }

}