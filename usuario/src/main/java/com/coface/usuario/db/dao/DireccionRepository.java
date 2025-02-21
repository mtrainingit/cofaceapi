package com.coface.usuario.db.dao;

import com.coface.usuario.db.model.Direccion;

public interface DireccionRepository {
    Direccion saveDireccion(Direccion direccion);
    Long deleteDireccionPorUsuarioId(Long id);
}
