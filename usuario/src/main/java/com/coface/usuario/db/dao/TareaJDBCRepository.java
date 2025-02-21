package com.coface.usuario.db.dao;

import com.coface.usuario.db.model.Tarea;
import com.coface.usuario.db.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.List;

public class TareaJDBCRepository implements TareaRepository {

    private final JdbcTemplate jdbcTemplate;

    public TareaJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Tarea saveTarea(Tarea tarea) {
        if (tarea.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                    (i) -> {
                        PreparedStatement ps = i.prepareStatement(
                                "insert into tareas (id, nombre, descripcion, usuario_id) values (tareas_id_seq.nextval, ?, ?, ?)",
                                new String[]{"id"}
                        );
                        ps.setString(1, tarea.getNombre());
                        ps.setString(2, tarea.getDescripcion());
                        ps.setLong(3, tarea.getUsuarioId());
                        return ps;
                    },
                    keyHolder
            );
            Long id = keyHolder.getKey().longValue();
            tarea.setId(id);
            return tarea;
        }
        else {
            jdbcTemplate.update(
                    "update tareas set nombre = ?, descripcion = ? where id = ?",
                    tarea.getNombre(),
                    tarea.getDescripcion(),
                    tarea.getId()
            );
            return tarea;
        }
    }

    @Override
    public List<Tarea> encontrarTareasPorUsuario(Usuario usuario) {
        return jdbcTemplate.query(
                "select * from tareas where usuario_id = ?",
                (result, rownum) -> new Tarea(
                        result.getLong("id"),
                        result.getString("nombre"),
                        result.getString("descripcion"),
                        usuario
                ),
                usuario.getId()
        );
    }

    @Override
    public Long deleteTareasPorUsuarioId(Long id) {
        jdbcTemplate.update(
                "delete from tareas where usuario_id = ?",
                id
        );
        return id;
    }
}
