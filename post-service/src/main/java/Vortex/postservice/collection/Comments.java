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
@Document(collection = "comments")
public class Comments {

    @Id
    private String commentID;
    private String postId;
    private String comment;
    private String commentUserEmail;
    private int commentLikeCount;
    private List<String> likedUserList;
    private List<String> replyCommentsIDList;

    public Comments(String postId, String comment, String commentUserEmail) {
        this.postId = postId;
        this.comment = comment;
        this.commentUserEmail = commentUserEmail;
    }
}
