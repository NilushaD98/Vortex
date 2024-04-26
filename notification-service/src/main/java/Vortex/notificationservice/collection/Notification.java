package Vortex.notificationservice.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "notification")
public class Notification {
    private String userEmail;
    private String postID;
    private String reactedUserEmail;
    private String reactedUserProfilePictureURL;
    private String reactionType;
    private String reactionCount;
    private String commentedCount;
    private String reactionMessage;
    private String commentMessage;
    private Date reactedTime;
}
