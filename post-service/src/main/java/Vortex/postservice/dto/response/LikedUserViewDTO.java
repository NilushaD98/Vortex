package Vortex.postservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LikedUserViewDTO {

    private String userEmail;
    private String userName;
    private String userProfilePictureURL;

}
