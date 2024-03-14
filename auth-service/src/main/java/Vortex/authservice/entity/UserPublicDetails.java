package Vortex.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "user_public_details")
public class UserPublicDetails {

    @Id
    private String id;
    private String userId;
    private String profilePhotoURL;
    private String bio;

    public UserPublicDetails(String userId, String profilePhotoURL, String bio) {
        this.userId = userId;
        this.profilePhotoURL=profilePhotoURL;
        this.bio=bio;
    }
}
