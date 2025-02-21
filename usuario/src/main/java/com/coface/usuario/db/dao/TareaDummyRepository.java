package com.coface.usuario.db.dao;

import com.coface.usuario.db.model.Tarea;
import com.coface.usuario.db.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TareaDummyRepository implements TareaRepository {

    private List<Tarea> tareas;

    public TareaDummyRepository() {
        tareas = new ArrayList<>();
    }

    @Override
    public Tarea saveTarea(Tarea tarea) {
        if (tarea.getId() == null) {
            Optional<Tarea> tareaConMayorId = tareas.stream().max((t1, t2) -> Long.compare(t1.getId(), t2.getId()));
            Long id = tareaConMayorId.isPresent() ? tareaConMayorId.get().getId() + 1 : 1L;
            tarea.setId(id);
            tareas.add(tarea);
        }
        else {
            Tarea tareaToModify = tareas.stream().filter(t -> t.getId() == tarea.getId()).findFirst().get();
            tareaToModify = tarea;
        }
        return tarea;
    }

    @Override
    public List<Tarea> encontrarTareasPorUsuario(Usuario usuario) {
        return tareas.stream().filter(i -> i.getUsuarioId() == usuario.getId()).collect(Collectors.toList());
    }

    @Override
    public Long deleteTareasPorUsuarioId(Long id) {
        tareas.removeIf(i -> i.getUsuarioId() == id);
        return id;
    }
}
