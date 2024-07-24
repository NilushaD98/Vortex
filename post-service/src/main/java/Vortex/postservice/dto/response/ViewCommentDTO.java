package Vortex.postservice.dto.response;

import Vortex.postservice.dto.request.UserByEmailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ViewCommentDTO {

    private String commentID;
    private String commentedUserEmail;
    private String commentedUserName;
    private String commentedUserProfilePictureURL;
    private String comment;
    private int commentLikeCount;
    private int replyCommentCount;
    private List<UserByEmailDTO> likedUsersList;
    private Boolean userLikedStatus;
    
}
