package Vortex.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoogleUserSignUpDTO {

    private String firstName;
    private String lastName;
    private Date birthDay;
    private String profilePhotoURL;
    private String email;
    private String contact;
    private String country;

}
