package Vortex.postservice.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "sharedpost")
public class SharedPost {

    @Id
    private String postID;
    private String sharedUserEmail;
    private String sharedComment;
    private Date sharedTime;
}
