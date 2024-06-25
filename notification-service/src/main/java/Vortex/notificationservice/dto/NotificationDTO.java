package Vortex.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDTO {

    private String notificationID;
    private String userEmail;
    private String notificationType;
    private String postID;
    private String reactedUserEmail;
    private String reactedUserName;
    private String reactedUserProfilePictureURL;
    private long reactionCount;
    private long commentedCount;
    private String reactionMessage;
    private String commentMessage;
    private String reactedTime;
    private Boolean readStatus;
    private String followedUserEmail;
    private String followedUserName;
    private String followedUserProfilePictureURL;
    private long followedCount;
    private String followingMessage;
}
