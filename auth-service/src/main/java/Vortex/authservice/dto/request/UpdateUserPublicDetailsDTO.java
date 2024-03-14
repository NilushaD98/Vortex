package Vortex.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserPublicDetailsDTO {

    private String userId;
    private String profilePhotoURL;
    private String bio;
}
