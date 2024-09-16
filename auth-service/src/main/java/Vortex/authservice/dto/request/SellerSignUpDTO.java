package Vortex.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerSignUpDTO {

    private String email;
    private String nic;
    private String metaMaskID;
    private String address;
    private String description;
    private String brandBanner;

}
