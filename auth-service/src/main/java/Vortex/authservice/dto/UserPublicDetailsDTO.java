package Vortex.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPublicDetailsDTO {
    @Id
    private String id;
    private String userId;
    private String profilePhotoURL;
    private String bio;
}
