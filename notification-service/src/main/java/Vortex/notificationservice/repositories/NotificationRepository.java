package Vortex.notificationservice.repositories;

import Vortex.notificationservice.collection.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<String, Notification> {
}
