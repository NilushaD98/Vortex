package Vortex.notificationservice.service.IMPL;

import Vortex.notificationservice.collection.Notification;
import Vortex.notificationservice.dto.NotificationDTO;
import Vortex.notificationservice.dto.PostLikeDTO;
import Vortex.notificationservice.dto.UserByEmailDTO;
import Vortex.notificationservice.feign.AuthServiceProxy;
import Vortex.notificationservice.repositories.NotificationRepository;
import Vortex.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceIMPL implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AuthServiceProxy authServiceProxy;


    @Override
    public Boolean add(NotificationDTO notificationDTO) {
        return null;
    }


    @KafkaListener(
            topics = "like",
            groupId = "notification",
            properties = {"spring.json.value.default.type=Vortex.notificationservice.dto.PostLikeDTO"})
    private void saveLikeNotification(PostLikeDTO postLikeDTO){
        log.info(postLikeDTO.toString());
        UserByEmailDTO likedUser = authServiceProxy.getFollowingDataList(List.of(postLikeDTO.getLikedUserEmail())).get(0);
        log.info(likedUser.toString());
        Optional<Notification> notificationOptional = notificationRepository.findNotificationByPostIDEqualsAndReactionType(postLikeDTO.getPostID(),"like");
        if(notificationOptional.isPresent()){
            Notification notification = notificationOptional.get();
            notification.setReactedUserEmail(postLikeDTO.getLikedUserEmail());
            notification.setReactedUserName(likedUser.getFirstName());
            notification.setReactedUserProfilePictureURL(likedUser.getProfilePhotoURL());
            notification.setReactionCount(notification.getReactionCount()+1);
            notification.setReactionMessage(" and "+notification.getReactionCount()+" others liked your post");
            notificationRepository.save(notification);
        }else {
            log.info("new ");
            Notification notification = new Notification();
            notification.setUserEmail(postLikeDTO.getPostAuthorEmail());
            notification.setNotificationType("like");
            notification.setPostID(postLikeDTO.getPostID());
            notification.setReactedUserEmail(postLikeDTO.getLikedUserEmail());
            notification.setReactedUserName(likedUser.getFirstName());
            notification.setReactedUserProfilePictureURL(likedUser.getProfilePhotoURL());
            notification.setReactionType("like");
            notification.setReactionCount(1);
            notification.setCommentedCount(0);
            notification.setReactionMessage(" liked your Post.");
            notification.setCommentMessage(" ");
            notification.setReactedTime(new Date());
            notificationRepository.save(notification);
        }
    }
}
