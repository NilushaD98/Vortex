package Vortex.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "seller_details")
public class SellerDetails {

    @Id
    private String sellerId;
    private String userId;
    private String nic;
    private String metaMaskID;
    private String address;
    private String description;
    private String brandBanner;

    public SellerDetails(String userId, String nic, String metaMaskID, String address,String description,String brandBanner) {
        this.userId = userId;
        this.nic = nic;
        this.metaMaskID = metaMaskID;
        this.address = address;
        this.description = description;
        this.brandBanner = brandBanner;
    }
}
