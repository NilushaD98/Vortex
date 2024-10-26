package Vortex.marketplace_service.dto.request;

import Vortex.marketplace_service.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrderStatusDTO {

    private String orderID;
    private OrderStatus orderStatus;
}
