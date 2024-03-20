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

    public SellerDetails(String userId, String nic, String metaMaskID, String address) {
        this.userId = userId;
        this.nic = nic;
        this.metaMaskID = metaMaskID;
        this.address = address;
    }
}
