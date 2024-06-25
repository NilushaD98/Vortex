package Vortex.notificationservice.service.IMPL;

import Vortex.notificationservice.collection.Notification;
import Vortex.notificationservice.dto.*;
import Vortex.notificationservice.feign.AuthServiceProxy;
import Vortex.notificationservice.repositories.NotificationRepository;
import Vortex.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class NotificationServiceIMPL implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AuthServiceProxy authServiceProxy;

    private Date getCurrentDateInUTC() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        return Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
    }

    private String formatToLocalTime(Date date) {
        ZonedDateTime utcZoned = date.toInstant().atZone(ZoneId.of("UTC"));
        ZonedDateTime localZoned = utcZoned.withZoneSameInstant(ZoneId.of("Asia/Colombo"));
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localZoned);
    }


    private void getDate(){
        List<Notification> all = notificationRepository.findAll();
        for(Notification notification : all){
            log.info(formatToLocalTime(notification.getReactedTime()));
        }
    }
    @KafkaListener(
            topics = "like",
            groupId = "notification",
            properties = {
                    "spring.json.type.mapping=Vortex.postservice.dto.request.PostLikeDTO:Vortex.notificationservice.dto.PostLikeDTO"
            }
    )
    private void saveLikeNotification(PostLikeDTO postLikeDTO){
        log.info(postLikeDTO.toString());
        UserByEmailDTO likedUser = authServiceProxy.getFollowingDataList(List.of(postLikeDTO.getLikedUserEmail())).get(0);
        log.info(likedUser.toString());
        Optional<Notification> notificationOptional = notificationRepository.findNotificationByPostIDEqualsAndNotificationType(postLikeDTO.getPostID(), "like");
        if(notificationOptional.isPresent()){
            Notification notification = notificationOptional.get();
            notification.setReactedUserEmail(postLikeDTO.getLikedUserEmail());
            notification.setReactedUserName(likedUser.getFirstName());
            notification.setReactedUserProfilePictureURL(likedUser.getProfilePhotoURL());
            notification.setReactionCount(notification.getReactionCount() + 1);
            notification.setReactedTime(getCurrentDateInUTC());
            notification.setReadStatus(false);
            notificationRepository.save(notification);
        } else {
            Notification notification = new Notification();
            notification.setUserEmail(postLikeDTO.getPostAuthorEmail());
            notification.setNotificationType("like");
            notification.setPostID(postLikeDTO.getPostID());
            notification.setReactedUserEmail(postLikeDTO.getLikedUserEmail());
            notification.setReactedUserName(likedUser.getFirstName());
            notification.setReactedUserProfilePictureURL(likedUser.getProfilePhotoURL());
            notification.setReactionType("like");
            notification.setReactionCount(1);
            notification.setReactedTime(getCurrentDateInUTC());
            notification.setReadStatus(false);
            notificationRepository.save(notification);
        }
        getDate();
    }
        @KafkaListener(
            topics = "comment",
            groupId = "notification",
            properties = {
                    "spring.json.type.mapping=Vortex.postservice.dto.request.AddCommentDTO:Vortex.notificationservice.dto.AddCommentDTO"
            }
        )
    private void saveCommentNotifications(AddCommentDTO addCommentDTO){
        log.info(addCommentDTO.toString());
        try {
            UserByEmailDTO commentedUser = authServiceProxy.getFollowingDataList(List.of(addCommentDTO.getCommentUserEmail())).get(0);
            Optional<Notification> notificationOptional = notificationRepository.findNotificationByPostIDEqualsAndNotificationType(addCommentDTO.getPostID(),"comment");
            if(notificationOptional.isPresent()){
                Notification notification = notificationOptional.get();
                notification.setReactedUserEmail(addCommentDTO.getCommentUserEmail());
                notification.setReactedUserName(commentedUser.getFirstName());
                notification.setReactedUserProfilePictureURL(commentedUser.getProfilePhotoURL());
                notification.setCommentedCount(notification.getCommentedCount()+1);
                notification.setReactedTime(getCurrentDateInUTC());
                notification.setReadStatus(false);
                notificationRepository.save(notification);
            }else {
                Notification notification = new Notification();
                notification.setUserEmail(addCommentDTO.getCommentUserEmail());
                notification.setNotificationType("comment");
                notification.setPostID(addCommentDTO.getPostID());
                notification.setReactedUserEmail(addCommentDTO.getCommentUserEmail());
                notification.setReactedUserName(commentedUser.getFirstName());
                notification.setReactedUserProfilePictureURL(commentedUser.getProfilePhotoURL());
                notification.setReactionCount(0);
                notification.setCommentedCount(1);
                notification.setReactedTime(getCurrentDateInUTC());
                notification.setReadStatus(false);
                notificationRepository.save(notification);
            }
        }catch (Exception e){
            log.error("error:::" + e.getMessage());
        }


    }

    @KafkaListener(
            topics = "follow",
            groupId = "notification",
            properties = {
                    "spring.json.type.mapping=Vortex.userservice.dto.request.FollowRequestDTO:Vortex.notificationservice.dto.FollowRequestDTO"
            }
    )
    public void saveFollowNotification(FollowRequestDTO followRequestDTO){
        Optional<Notification> notificationOptional = notificationRepository.findNotificationByUserEmailEqualsAndNotificationTypeAndReadStatusFalse(followRequestDTO.getFollowingEmail(), "follow");
        UserByEmailDTO followUser = authServiceProxy.getFollowingDataList(List.of(followRequestDTO.getEmail())).get(0);
        if(notificationOptional.isPresent()){
            Notification notification = notificationOptional.get();
            notification.setReactedUserEmail(followRequestDTO.getFollowingEmail());
            notification.setReactedUserName(followUser.getFirstName() +" "+followUser.getLastName());
            notification.setReactedUserProfilePictureURL(followUser.getProfilePhotoURL());
            notification.setReactionCount(notification.getReactionCount() + 1);
            notification.setReactedTime(getCurrentDateInUTC());
            notification.setReadStatus(false);
            notificationRepository.save(notification);
        }else{
            Notification notification = new Notification();
            notification.setUserEmail(followRequestDTO.getFollowingEmail());
            notification.setNotificationType("follow");
            notification.setReactedUserEmail(followRequestDTO.getEmail());
            notification.setReactedUserName(followUser.getFirstName() +" "+followUser.getLastName());
            notification.setReactedUserProfilePictureURL(followUser.getProfilePhotoURL());
            notification.setReactionCount(1);
            notification.setReactedTime(getCurrentDateInUTC());
            notification.setReadStatus(false);
            notificationRepository.save(notification);

        }
    }
    @Override
    public Boolean add(NotificationDTO notificationDTO) {
        return null;
    }

    @Override
    public List<NotificationDTO> getAllNotification(String userEmail){
        List<Notification> notificationList = notificationRepository.findNotificationByUserEmailEqualsOrderByReactedTimeDesc(userEmail);
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification:notificationList){
            NotificationDTO notificationDTO = new NotificationDTO();
            if(notification.getNotificationType().equals("like")){
                notificationDTO.setNotificationID(notification.getNotificationID());
                notificationDTO.setNotificationType(notification.getNotificationType());
                notificationDTO.setPostID(notification.getPostID());
                notificationDTO.setReactedUserEmail(notification.getReactedUserEmail());
                notificationDTO.setReactedUserName(notification.getReactedUserName());
                notificationDTO.setReactedUserProfilePictureURL(notification.getReactedUserProfilePictureURL());
                notificationDTO.setReactionCount(notification.getReactionCount());
                if(notification.getReactionCount() ==1){
                    notificationDTO.setReactionMessage(" liked your post.");
                }else {
                    notificationDTO.setReactionMessage(" and "+(notification.getReactionCount()-1)+" others liked your post");
                }
            }
            else if(notification.getNotificationType().equals("comment")){
                notificationDTO.setNotificationID(notification.getNotificationID());
                notificationDTO.setNotificationType(notification.getNotificationType());
                notificationDTO.setPostID(notification.getPostID());
                notificationDTO.setReactedUserEmail(notification.getReactedUserEmail());
                notificationDTO.setReactedUserName(notification.getReactedUserName());
                notificationDTO.setReactedUserProfilePictureURL(notification.getReactedUserProfilePictureURL());
                notificationDTO.setCommentedCount(notification.getCommentedCount());
                if(notification.getCommentedCount() ==1){
                    notificationDTO.setCommentMessage(" commented your post.");
                }else {
                    notificationDTO.setCommentMessage(" and "+(notification.getCommentedCount()-1) +" others commented your post");
                }
            }
            else if (notification.getNotificationType().equals("follow")){
                notificationDTO.setNotificationID(notification.getNotificationID());
                notificationDTO.setNotificationType(notification.getNotificationType());
                notificationDTO.setFollowedUserEmail(notification.getReactedUserEmail());
                notificationDTO.setFollowedUserName(notification.getReactedUserName());
                notificationDTO.setFollowedUserProfilePictureURL(notification.getReactedUserProfilePictureURL());
                notificationDTO.setFollowedCount(notification.getReactionCount());
                if(notification.getReactionCount() ==1 ){
                    notificationDTO.setFollowingMessage(" followed you.");
                }else{
                    notificationDTO.setFollowingMessage(" and "+ (notification.getReactionCount()-1) +" others followed you.");
                }
            }
            notificationDTO.setReactedTime(formatToLocalTime(notification.getReactedTime()));
            notificationDTO.setReadStatus(notification.getReadStatus());
            notificationDTOList.add(notificationDTO);
        }
        return notificationDTOList;
    }
}
