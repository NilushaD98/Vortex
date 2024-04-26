package Vortex.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDTO {

    private String userEmail;
    private String postID;
    private String reactedUserEmail;
    private String reactedUserName;
    private String reactedUserProfilePictureURL;
    private String reactionType;
    private String reactionCount;
    private String commentedCount;
    private String reactionMessage;
    private String commentMessage;
}
