package Vortex.postservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserByEmailDTO {

    private String profilePhotoURL;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
}
