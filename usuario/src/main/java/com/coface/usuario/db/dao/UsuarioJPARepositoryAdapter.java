package com.coface.usuario.db.dao;

import com.coface.usuario.db.model.Tarea;
import com.coface.usuario.db.model.Usuario;
import com.coface.usuario.db.model.UsuarioReducidoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class UsuarioJPARepositoryAdapter implements UsuarioRepository {

    @PersistenceContext
    private EntityManager em;

    private final UsuarioJPARepository usuarioJPARepository;

    public UsuarioJPARepositoryAdapter(UsuarioJPARepository usuarioJPARepository) {
        this.usuarioJPARepository = usuarioJPARepository;
    }

    @Override
    public List<Usuario> getUsuarios() {
        return usuarioJPARepository.findAll();
    }

    @Override
    public Optional<Usuario> getUsuarioPorId(Long id) {
        return usuarioJPARepository.findById(id);
    }

    @Transactional
    @Override
    public Usuario saveUsuario(Usuario usuario) {
        if (usuario.getId() == null) {
            Long id = usuarioJPARepository.save(usuario).getId();
            usuario.setId(id);
            return usuario;
        }
        else {
            return usuarioJPARepository.save(usuario);
        }
    }

    @Transactional
    @Override
    public Long deleteUsuario(Long id) {
        usuarioJPARepository.deleteById(id);
        return id;
    }

    @Override
    public boolean existeUsuarioPorId(Long id) {
        return usuarioJPARepository.existsById(id);
    }

    @Override
    public boolean existeUsuarioPorEmail(String email) {
        return usuarioJPARepository.existsByEmail(email);
    }

    @Override
    public Page<Usuario> getUsuariosPaginados(int pagina, int tamano, String ordPor, String dirOrd) {
        Sort sort = dirOrd.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordPor).ascending() : Sort.by(ordPor).descending();
        Pageable pageable = PageRequest.of(pagina, tamano, sort);
        return usuarioJPARepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Tarea> encontrarTareasPorUsuario(Usuario usuario) {
        return usuario.getTareas();
    }

    @Override
    public List<UsuarioReducidoDTO> getUsuariosReducidos() {
        return usuarioJPARepository.findUsuariosReducidosJPQL();
    }

    @Override
    public Optional<Usuario> getUsuarioPorEmail(String email) {
        return usuarioJPARepository.findByEmail(email);
    }

    @Transactional
    Long insertarUsuarioStoredProcedureQuery(Usuario usuario) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("insertar_usuario");
        query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_password", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_rol", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_id", Long.class, ParameterMode.OUT);

        query.setParameter("p_nombre", usuario.getNombre());
        query.setParameter("p_email", usuario.getEmail());
        query.setParameter("p_password", usuario.getPassword());
        query.setParameter("p_rol", usuario.getRol());

        query.execute();

        return (Long) query.getOutputParameterValue("p_id");
    }
}
