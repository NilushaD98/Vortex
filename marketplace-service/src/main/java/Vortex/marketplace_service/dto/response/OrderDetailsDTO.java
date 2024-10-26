package Vortex.marketplace_service.dto.response;

import Vortex.marketplace_service.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDetailsDTO {

    private String orderID;
    private String itemID;
    private String itemName;
    private String itemImageURL;
    private double itemPrice;
    private int quantity;
    private String buyerID;
    private String sellerID;
    private double price;
    private double paidPrice;
    private String address;
    private String buyerWalletID;
    private String sellerWalletID;
    private OrderStatus orderStatus;

}
