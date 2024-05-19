package Vortex.postservice.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "replycomment")
public class ReplyComment {

    @Id
    private String repliedCommentID;
    private String repliedComment;
    private String repliedUserEmail;
    private String mainCommentID;
    private int likedCount;
    private List<String> likedUserEmailList;

    public ReplyComment(String repliedCommentID, String repliedComment, String repliedUserEmail, String mainCommentID) {
        this.repliedCommentID = repliedCommentID;
        this.repliedComment = repliedComment;
        this.repliedUserEmail = repliedUserEmail;
        this.mainCommentID = mainCommentID;
    }
}
