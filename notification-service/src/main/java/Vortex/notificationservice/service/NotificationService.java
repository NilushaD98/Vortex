package Vortex.notificationservice.service;

import Vortex.notificationservice.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    Boolean add(NotificationDTO notificationDTO);
    List<NotificationDTO> getAllNotification(String userEmail);
}
