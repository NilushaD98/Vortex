package Vortex.postservice.dto.response;

import Vortex.postservice.dto.request.UserByEmailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ViewReplyCommentDTO {

    @Id
    private String repliedCommentID;
    private String repliedComment;
    private String repliedUserEmail;
    private String repliedUserName;
    private String repliedUserProfilePictureURL;
    private String mainCommentID;
    private int likedCount;
    private List<UserByEmailDTO> likedUsersList;
    private Boolean userLikedStatus;


}
