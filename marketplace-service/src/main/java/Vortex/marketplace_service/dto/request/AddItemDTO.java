package Vortex.marketplace_service.dto.request;

import Vortex.marketplace_service.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddItemDTO {

    private String itemID;
    private String itemName;
    private String itemDescription;
    private String itemImageURL;
    private Double price;
    private String sellerID;
    private String sellerWalletID;
    private int quantity;
    private ItemStatus itemStatus;

}
