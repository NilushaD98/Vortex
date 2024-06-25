package Vortex.notificationservice.repositories;

import Vortex.notificationservice.collection.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    Optional<Notification> findNotificationByPostIDEqualsAndNotificationType(String postID,String notificationType);

    List<Notification> findNotificationByUserEmailEqualsOrderByReactedTimeDesc(String userEmail);

    Optional<Notification> findNotificationByUserEmailEqualsAndNotificationTypeAndReadStatusFalse(String followingEmail,String notificationType);
}
