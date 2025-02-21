package com.coface.usuario.db.dao;

import com.coface.usuario.db.model.Direccion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;

public class DireccionJDBCRepository implements DireccionRepository {

    private final JdbcTemplate jdbcTemplate;

    public DireccionJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Direccion saveDireccion(Direccion direccion) {
        if (direccion.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                    (i) -> {
                        PreparedStatement ps = i.prepareStatement(
                                "insert into direcciones (id, direccion, codigo_postal, usuario_id) values (direcciones_id_seq.nextval, ?, ?, ?)",
                                new String[]{"id"}
                        );
                        ps.setString(1, direccion.getDireccion());
                        ps.setString(2, direccion.getCodigoPostal());
                        ps.setLong(3, direccion.getUsuario().getId());
                        return ps;
                    },
                    keyHolder
            );
            Long id = keyHolder.getKey().longValue();
            direccion.setId(id);
            return direccion;
        }
        else {
            jdbcTemplate.update(
                    "update direcciones set direccion = ?, codigo_postal = ? where id = ?",
                    direccion.getDireccion(),
                    direccion.getCodigoPostal(),
                    direccion.getId()
            );
            return direccion;
        }
    }

    @Override
    public Long deleteDireccionPorUsuarioId(Long id) {
        jdbcTemplate.update(
                "delete from direcciones where usuario_id = ?",
                id
        );
        return id;
    }
}
