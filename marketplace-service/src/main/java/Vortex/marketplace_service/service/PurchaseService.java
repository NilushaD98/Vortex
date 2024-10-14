package Vortex.marketplace_service.service;

import Vortex.marketplace_service.dto.request.OrderDTO;
import Vortex.marketplace_service.dto.response.OrderSuccessDTO;

public interface PurchaseService {
    OrderSuccessDTO order(OrderDTO orderDTO);
}
