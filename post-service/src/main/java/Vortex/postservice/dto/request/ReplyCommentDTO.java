package Vortex.postservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyCommentDTO {

    private String repliedComment;
    private String repliedUserEmail;
    private String mainCommentID;
}
