package com.coface.usuario.api.controller;

import com.coface.usuario.db.model.Tarea;
import com.coface.usuario.db.model.UsuarioReducidoDTO;
import com.coface.usuario.mapper.UsuarioAUsuarioResponseDTOMapper;
import com.coface.usuario.api.dto.UsuarioCreateRequestDTO;
import com.coface.usuario.api.dto.UsuarioResponseDTO;
import com.coface.usuario.api.dto.UsuarioUpdateRequestDTO;
import com.coface.usuario.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioAUsuarioResponseDTOMapper usuarioResponseDTOMapper;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);


    public UsuarioController(UsuarioService usuarioService, UsuarioAUsuarioResponseDTOMapper usuarioResponseDTOMapper) {
        this.usuarioService = usuarioService;
        this.usuarioResponseDTOMapper = usuarioResponseDTOMapper;
    }

    @GetMapping()
    public List<UsuarioResponseDTO> getUsuarios() {
        logger.info("Obteniendo lista de usuarios");
        return usuarioService.getUsuarios().stream().map(usuarioResponseDTOMapper).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public UsuarioResponseDTO getUsuarioPorId(@PathVariable Long id) {
        logger.info("Obteniendo usuario por id");
        return usuarioResponseDTOMapper.apply(usuarioService.getUsuarioPorId(id));
    }

    @PostMapping
    public ResponseEntity<Long> postUsuario(@RequestBody UsuarioCreateRequestDTO usuarioCreateRequestDTO) {
        logger.info("Creando usuario");
        return new ResponseEntity<>(usuarioService.crearUsusario(usuarioCreateRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateUsuario(@PathVariable Long id, @RequestBody UsuarioUpdateRequestDTO usuarioUpdateRequestDTO) {
        logger.info("Actualizando usuario");
        return new ResponseEntity<>(usuarioService.actualizarUsuario(id, usuarioUpdateRequestDTO), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteUsuario(@PathVariable Long id) {
        logger.info("Eliminando usuario");
        return new ResponseEntity<>(usuarioService.eliminarUsuario(id), HttpStatus.OK);
    }

    @GetMapping("paginado")
    public PagedModel<UsuarioResponseDTO> getUsuariosPaginados(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano,
            @RequestParam(defaultValue = "nombre") String ordPor,
            @RequestParam(defaultValue = "asc") String dirOrd
    ) {
        logger.info("Obteniendo lista de usuarios paginados");
        return new PagedModel<>(usuarioService.getUsuariosPaginados(pagina, tamano, ordPor, dirOrd).map(usuarioResponseDTOMapper));
    }

    @PutMapping("{id}/tarea")
    public Long asignarTarea(@PathVariable Long id, @RequestBody Tarea tarea) {
        logger.info("Asignando tarea a usuario");
        usuarioService.asignarTarea(id, tarea);
        return id;
    }

    @GetMapping("{id}/tarea")
    public UsuarioResponseDTO getTareasDeUsuario(@PathVariable Long id) {
        logger.info("Obteniendo usuario con lista de tareas");
        return usuarioResponseDTOMapper.apply(usuarioService.getTareasDeUsuario(id));
    }

    @GetMapping("reducido")
    public List<UsuarioReducidoDTO> getUsuariosReducidos() {
        logger.info("Obteniendo lista de usuarios reducidos");
        return usuarioService.getUsuariosReducidos();
    }
}
