package Vortex.marketplace_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ItemReviewDTO {

    private String orderID;
    private String buyerID;
    private String buyerName;
    private String buyerImageURl;
    private int rate;
    private String rateMessage;
}
