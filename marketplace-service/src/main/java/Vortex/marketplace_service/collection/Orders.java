package Vortex.marketplace_service.collection;

import Vortex.marketplace_service.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "orders")
public class Orders {

    @Id
    private String orderID;
    private String transactionID;
    private String itemID;
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
