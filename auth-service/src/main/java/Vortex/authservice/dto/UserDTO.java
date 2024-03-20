package Vortex.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private Date birthDay;
    private String contact;
    private String country;
    private String profilePhotoURL;
    private String bio;
    private boolean followedStatus;
}
