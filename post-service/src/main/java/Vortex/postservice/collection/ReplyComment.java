package Vortex.postservice.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyComment {

    private String repliedCommentID;
    private String repliedComment;
    private String repliedUserEmail;
    private String mainCommentID;
    private int likedCount;
    private List<String> likedUserEmailList;
}
