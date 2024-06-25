package Vortex.notificationservice.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "notification")
public class Notification {

    @Id
    private String notificationID;
    private String userEmail;
    private String notificationType;
    private String postID;
    private String postCaption;
    private String reactedUserEmail;
    private String reactedUserName;
    private String reactedUserProfilePictureURL;
    private String reactionType;
    private long reactionCount;
    private long commentedCount;
    private String reactionMessage;
    private String commentMessage;
    private Date reactedTime;
    private Boolean readStatus;
}
