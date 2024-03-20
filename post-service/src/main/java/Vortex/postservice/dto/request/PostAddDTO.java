package Vortex.postservice.dto.request;

import Vortex.postservice.collection.Comments;
import Vortex.postservice.enums.PostPublicStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostAddDTO {

    private String postAuthorEmail;
    private String mediaLink;
    private String caption;
    private Date postedTime;
    private PostPublicStatus postPublicStatus;
    private List<String> taggedUserEmailList;
}
