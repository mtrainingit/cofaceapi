package com.coface.notificacion;

import com.coface.common.NotificacionCreateRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    @KafkaListener(topics = "usuario.created", groupId = "notification-group")
    public void listen(NotificacionCreateRequestDTO notificacionCreateRequestDTO) {
        logger.info("Creando notificacion {}", notificacionCreateRequestDTO);
    }
}
