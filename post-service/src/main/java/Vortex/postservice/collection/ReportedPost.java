package Vortex.postservice.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "reportedposts")
public class ReportedPost {

    @Id
    private String postID;
    private String reportedUserEmail;
    private String reportedReason;
}
