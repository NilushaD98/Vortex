package Vortex.postservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LikeReplyCommentDTO {

    private String mainCommentID;
    private String repliedCommentID;
    private String likedUserEmail;
}
