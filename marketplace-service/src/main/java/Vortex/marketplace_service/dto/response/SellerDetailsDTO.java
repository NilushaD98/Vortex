package Vortex.marketplace_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SellerDetailsDTO {

    private String sellerId;
    private String userId;
    private String nic;
    private String metaMaskID;
    private String address;
    private String description;
    private String brandBanner;
}
