package Vortex.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoogleSignUpDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String profilePhotoURL;

}
