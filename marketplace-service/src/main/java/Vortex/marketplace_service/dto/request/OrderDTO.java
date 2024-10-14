package Vortex.marketplace_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDTO {

    private String itemID;
    private int quantity;
    private String buyerID;
    private String sellerID;
    private String buyerWalletID;
    private String sellerWalletID;
    private String address;

}
