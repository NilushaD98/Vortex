package Vortex.marketplace_service.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "item_review")
public class ItemReview {

    @Id
    private String reviewID;
    private String orderID;
    private String itemID;
    private int rate;
    private String rateMessage;

    public ItemReview(String orderID, String itemID, int rate, String rateMessage) {
        this.orderID = orderID;
        this.itemID = itemID;
        this.rate = rate;
        this.rateMessage = rateMessage;
    }
}
