package Vortex.postservice.collection;

import Vortex.postservice.enums.PostPublicStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "post")
public class Post {

    @Id
    private String postID;
    private String postAuthorEmail;
    private String mediaLink;
    private String caption;
    private Date postedTime;
    private long likeCount;
    private long commentCount;
    private PostPublicStatus postPublicStatus;
    private List<String> taggedUseEmailList;


    public Post(String postAuthorEmail, String mediaLink, String caption, Date postedTime, long likeCount, long commentCount, PostPublicStatus postPublicStatus,List<String> taggedUseEmailList) {
        this.postAuthorEmail = postAuthorEmail;
        this.mediaLink = mediaLink;
        this.caption = caption;
        this.postedTime = postedTime;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.postPublicStatus = postPublicStatus;
        this.taggedUseEmailList = taggedUseEmailList;
    }
}
