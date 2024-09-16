package Vortex.marketplace_service.dto;

import Vortex.marketplace_service.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDTO {
    private String itemID;
    private String itemName;
    private String itemDescription;
    private ItemStatus itemStatus;
    private String itemImageURL;
    private Double price;/*ethereum*/
    private String sellerID;
    private int quantity;

}
