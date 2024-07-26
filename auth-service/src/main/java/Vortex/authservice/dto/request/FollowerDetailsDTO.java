package Vortex.authservice.dto.request;

import Vortex.authservice.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowerDetailsDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String profilePhotoURL;
    private String country;

}
