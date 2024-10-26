package Vortex.marketplace_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RateOrderDTO {

    private String orderID;
    private int rate;
    private String rateMessage;
}
