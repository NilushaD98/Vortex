package Vortex.authservice.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class UserSignUpDTO {

    private String firstName;
    private String lastName;
    private Date birthDay;
    private String email;
    private String contact;
    private String country;
    private String password;
}
