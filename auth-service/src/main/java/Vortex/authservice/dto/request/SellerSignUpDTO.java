package Vortex.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerSignUpDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String contact;
    private String nic;
    private String metaMaskID;
    private String country;
    private String address;
    private String password;
}
