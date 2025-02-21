package com.coface.usuario.config.wiring;

import com.coface.usuario.db.dao.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class UsuarioConfig {

    @Bean
    @Qualifier("direccion-dummy")
    public DireccionRepository getDireccionDummyRepository() {
        return new DireccionDummyRepository();
    }

    @Bean
    @Qualifier("tarea-dummy")
    public TareaRepository getTareaDummyRepository() {
        return new TareaDummyRepository();
    }

    @Bean
    @Qualifier("usuario-dummy")
    public UsuarioRepository getUsuarioDummyRepository(
            @Qualifier("direccion-dummy") DireccionRepository direccionRepository,
            @Qualifier("tarea-dummy") TareaRepository tareaRepository
    ) {
        return new UsuarioDummyRepository(direccionRepository, tareaRepository);
    }

    @Bean
    @Qualifier("direccion-jdbc")
    public DireccionRepository getDireccionJDBCRepository(JdbcTemplate jdbcTemplate) {
        return new DireccionJDBCRepository(jdbcTemplate);
    }

    @Bean
    @Qualifier("tarea-jdbc")
    public TareaRepository getTareaJDBCRepository(JdbcTemplate jdbcTemplate) {
        return new TareaJDBCRepository(jdbcTemplate);
    }

    @Bean
    @Qualifier("usuario-jdbc")
    public UsuarioRepository getUsuarioJDBCRepository(
            JdbcTemplate jdbcTemplate,
            @Qualifier("direccion-jdbc") DireccionRepository direccionRepository,
            @Qualifier("tarea-jdbc") TareaRepository tareaRepository
    ) {
        return new UsuarioJDBCRepository(jdbcTemplate, direccionRepository, tareaRepository);
    }

    @Bean
    @Qualifier("usuario-jpa")
    public UsuarioRepository getUsuarioJPARepositoryAdapter(UsuarioJPARepository usuarioJPARepository) {
        return new UsuarioJPARepositoryAdapter(usuarioJPARepository);
    }

}
