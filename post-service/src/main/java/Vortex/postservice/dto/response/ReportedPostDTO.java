package Vortex.postservice.dto.response;

import Vortex.postservice.enums.PostPublicStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportedPostDTO {

    private String postID;
    private String reportedUserEmail;
    private String reportedUserName;
    private String reportedUserProfilePhotoURL;
    private String reportedReason;
    private String postAuthorName;
    private String postAuthorEmail;
    private String postAuthorProfilePhotoURL;
    private String mediaLink;
    private String caption;
    private Date postedTime;
    private long likeCount;
    private long commentCount;


}
