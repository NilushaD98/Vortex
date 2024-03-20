package Vortex.postservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostLikeDTO {

    private String postID;
    private String postAuthorEmail;
    private String likedUserEmail;
}
