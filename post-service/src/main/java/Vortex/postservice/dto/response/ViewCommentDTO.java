package Vortex.postservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ViewCommentDTO {

    private String commentedUserEmail;
    private String commentedUserName;
    private String commentedUserProfilePictureURL;
    private String comment;
}
