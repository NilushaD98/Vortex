package Vortex.marketplace_service.collection;

import Vortex.marketplace_service.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "item")
public class Item {

    @Id
    private String itemID;
    private String itemName;
    private String itemDescription;
    private ItemStatus itemStatus;
    private String itemImageURL;
    private Double price;/*ethereum*/
    private String sellerID;
    private int quantity;



}
