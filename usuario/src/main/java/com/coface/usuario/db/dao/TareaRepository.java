package com.coface.usuario.db.dao;

import com.coface.usuario.db.model.Tarea;
import com.coface.usuario.db.model.Usuario;

import java.util.List;

public interface TareaRepository {

    Tarea saveTarea(Tarea tarea);

    List<Tarea> encontrarTareasPorUsuario(Usuario usario);

    Long deleteTareasPorUsuarioId(Long id);
}
