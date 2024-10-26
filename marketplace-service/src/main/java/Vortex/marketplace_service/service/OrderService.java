package Vortex.marketplace_service.service;

import Vortex.marketplace_service.dto.request.RateOrderDTO;
import Vortex.marketplace_service.dto.response.OrderDetailsDTO;

import java.util.List;

public interface OrderService {
    List<OrderDetailsDTO> getAllOrdersByBuyerID(String buyerID);
    List<OrderDetailsDTO> getAllOrdersBySellerID(String sellerID);


    Boolean rateOrder(RateOrderDTO rateOrderDTO);
}
