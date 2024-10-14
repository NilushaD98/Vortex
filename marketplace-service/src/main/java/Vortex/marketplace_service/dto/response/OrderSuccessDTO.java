package Vortex.marketplace_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderSuccessDTO {

    private Boolean placedStatus;
    private String orderID;
}
