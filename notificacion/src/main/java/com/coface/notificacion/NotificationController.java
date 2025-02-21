package com.coface.notificacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notificacion")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping
    public ResponseEntity<?> crearNotificacion(@RequestBody NotificacionCreateRequestDTO notificacionCreateRequestDTO) {
        logger.info("Creando notificacion {}", notificacionCreateRequestDTO);
        return ResponseEntity.ok(notificacionCreateRequestDTO.id());
    }
}
