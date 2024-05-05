package Vortex.postservice.dto.request;

import Vortex.postservice.collection.ReplyComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddCommentDTO {

    private String postID;
    private String commentUserEmail;
    private String comment;

}
