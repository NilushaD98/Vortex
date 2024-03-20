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
public class PostViewDTO {

    private String postID;
    private String postAuthorEmail;
    private String postAuthor;
    private String mediaLink;
    private String caption;
    private Date postedTime;
    private long likeCount;
    private long commentCount;
    private PostPublicStatus postPublicStatus;
    private List<String> taggedUserEmailList;
    private boolean viewedUserLikedStatus;
}
