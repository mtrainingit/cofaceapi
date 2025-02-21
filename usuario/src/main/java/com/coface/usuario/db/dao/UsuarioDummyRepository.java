package com.coface.usuario.db.dao;

import com.coface.usuario.db.model.Tarea;
import com.coface.usuario.db.model.Usuario;
import com.coface.usuario.db.model.UsuarioReducidoDTO;
import com.coface.usuario.exception.CampoOrdenDesconocido;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioDummyRepository implements UsuarioRepository {

    private List<Usuario> usuarios;
    private DireccionRepository direccionRepository;
    private TareaRepository tareaRepository;

    public UsuarioDummyRepository(
            DireccionRepository direccionRepository,
            TareaRepository tareaRepository
    ) {
        this.usuarios = new ArrayList<>();
        this.direccionRepository = direccionRepository;
        this.tareaRepository = tareaRepository;
    }

    @Override
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    @Override
    public Optional<Usuario> getUsuarioPorId(Long id) {
        return usuarios.stream().filter(u -> u.getId() == id).findFirst();
    }

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        if (usuario.getId() == null) {
            direccionRepository.saveDireccion(usuario.getDireccion());
            Optional<Usuario> usuarioConMayorId = usuarios.stream().max((u1, u2) -> Long.compare(u1.getId(), u2.getId()));
            Long id = usuarioConMayorId.isPresent() ? usuarioConMayorId.get().getId() + 1 : 1L;
            usuario.setId(id);
            usuarios.add(usuario);
            for (Tarea tarea : usuario.getTareas()) {
                tareaRepository.saveTarea(tarea);
            }
        }
        else {
            direccionRepository.saveDireccion(usuario.getDireccion());
            Usuario usuarioToModify = usuarios.stream().filter(u -> u.getId() == usuario.getId()).findFirst().get();
            usuarioToModify = usuario;
            for (Tarea tarea : usuario.getTareas()) {
                tareaRepository.saveTarea(tarea);
            }
        }
        return usuario;
    }

    @Override
    public Long deleteUsuario(Long id) {
        usuarios.remove(usuarios.stream().filter(u -> u.getId() == id).findFirst().get());;
        return id;
    }

    @Override
    public boolean existeUsuarioPorId(Long id) {
        return usuarios.stream().filter(u -> u.getId() == id).findFirst().isPresent();
    }

    @Override
    public boolean existeUsuarioPorEmail(String email) {
        return usuarios.stream().filter(u -> u.getEmail().equals(email)).findFirst().isPresent();
    }

    @Override
    public Page<Usuario> getUsuariosPaginados(int pagina, int tamano, String ordPor, String dirOrd) {
        Sort sort;
        if (dirOrd.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            sort = Sort.by(ordPor).ascending();
        }
        else if (dirOrd.equalsIgnoreCase(Sort.Direction.DESC.name())) {
            sort = Sort.by(ordPor).descending();
        }
        else {
            sort = Sort.unsorted();
        }
        Comparator<Usuario> comparator = null;
        List<Usuario> usuariosPaginados = usuarios;
        if (sort != Sort.unsorted()) {
            for (Sort.Order order : sort) {
                Comparator<Usuario> currentComparator = null;
                switch (order.getProperty()) {
                    case "id":
                        currentComparator = Comparator.comparing(Usuario::getId);
                        currentComparator = (order.isAscending()) ? currentComparator : currentComparator.reversed();
                        break;
                    case "nombre":
                        currentComparator = Comparator.comparing(Usuario::getNombre, String.CASE_INSENSITIVE_ORDER);
                        currentComparator = (order.isAscending()) ? currentComparator : currentComparator.reversed();
                        break;
                    case "email":
                        currentComparator = Comparator.comparing(Usuario::getEmail, String.CASE_INSENSITIVE_ORDER);
                        currentComparator = (order.isAscending()) ? currentComparator : currentComparator.reversed();
                        break;
                    default:
                        throw new CampoOrdenDesconocido("El campo a ordenar " + order.getProperty() + " es desconocido");
                }
                comparator = (comparator == null) ? currentComparator : comparator.thenComparing(currentComparator);
            }
            usuariosPaginados = usuarios.stream().sorted(comparator).collect(Collectors.toList());
        }
        Pageable pageable = PageRequest.of(pagina, tamano, sort);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + tamano, usuariosPaginados.size());
        if (start > end) {
            usuariosPaginados = new ArrayList<>();
        }
        else {
            usuariosPaginados = usuariosPaginados.subList(start, end);
        }
        return new PageImpl<>(usuariosPaginados, pageable, usuarios.size());
    }

    @Override
    public List<Tarea> encontrarTareasPorUsuario(Usuario usuario) {
        return tareaRepository.encontrarTareasPorUsuario(usuario);
    }

    @Override
    public List<UsuarioReducidoDTO> getUsuariosReducidos() {
        return usuarios.stream().map(i -> new UsuarioReducidoDTO(
                i.getNombre(),
                i.getEmail()
        )).collect(Collectors.toList());
    }

    @Override
    public Optional<Usuario> getUsuarioPorEmail(String email) {
        return usuarios.stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

}
