package Vortex.postservice.dto;

import Vortex.postservice.dto.request.ReplyCommentDTO;
import Vortex.postservice.dto.response.ViewCommentDTO;
import Vortex.postservice.dto.response.ViewReplyCommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentsDTO {

    private List<ViewCommentDTO> mainComments;
    private List<List<ViewReplyCommentDTO>> repliesComment;
}
