package Vortex.userservice.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "following")
public class Following {

    @Id
    private String userID;
    private String userEmail;
    private List<String> followingUserEmailList;

    public Following(String userEmail, List<String> followingUserEmailList) {
        this.userEmail = userEmail;
        this.followingUserEmailList = followingUserEmailList;
    }
}
