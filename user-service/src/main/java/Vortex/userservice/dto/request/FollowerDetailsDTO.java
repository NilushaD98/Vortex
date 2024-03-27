package Vortex.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowerDetailsDTO {

    private String userEmail;
    private String profilePhotoURL;
    private String Name;
}
