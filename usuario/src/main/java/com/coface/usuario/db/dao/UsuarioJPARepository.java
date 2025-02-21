package com.coface.usuario.db.dao;

import com.coface.usuario.db.model.Usuario;
import com.coface.usuario.db.model.UsuarioReducidoDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioJPARepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
    // JPQL
    @Query("select new com.coface.usuario.db.model.UsuarioReducidoDTO(u.nombre, u.email) from Usuario u")
    List<UsuarioReducidoDTO> findUsuariosReducidosJPQL();

    // Native query
    @Query(value = "select nombre, email from usuarios", nativeQuery = true)
    List<UsuarioReducidoDTO> findUsuariosReducidosNativeQuery();

    /*
     * funcional
     * (si
     *     1 - oracle tiene configurada la ejecución por nombre sin call
     *     2 - la función tiene el mismo nombre del stored procedure al que se llama
     * )
     */
    @Transactional
    @Procedure(
            procedureName = "insertar_usuario"
    )
    Long insertarUsuario(
            @Param("p_nombre") String nombre,
            @Param("p_email") String email,
            @Param("p_password") String password,
            @Param("p_rol") Integer rol
    );

    /*
     * funcional
     * (si
     *     1 - cumplen las condiciones del anterior ejemplo
     *     2 - necesita más control en el mapping
     * )
     */
    @Transactional
    @Procedure(
            name = "Usuario.insertarUsuarioNamedQuery"
    )
    Long insertarUsuarioNamedQuery(
            @Param("p_nombre") String nombre,
            @Param("p_email") String email,
            @Param("p_password") String password,
            @Param("p_rol") Integer rol
    );

    /*
     * funcional
     * (pero no apto para traer el parámetro de salida de vuelta)
     */
    @Transactional
    @Query(
            value = "call insertar_usuario(:p_nombre, :p_email, :p_password, :p_rol, :p_id)",
            nativeQuery = true
    )
    void insertarUsuarioQuery(
            @Param("p_nombre") String nombre,
            @Param("p_email") String email,
            @Param("p_password") String password,
            @Param("p_rol") Integer rol,
            @Param("p_id") Long id
    );

}
