package Vortex.marketplace_service.service.IMPL;

import Vortex.marketplace_service.collection.Item;
import Vortex.marketplace_service.collection.Orders;
import Vortex.marketplace_service.dto.response.OrderDetailsDTO;
import Vortex.marketplace_service.repository.ItemRepository;
import Vortex.marketplace_service.repository.OrderRepository;
import Vortex.marketplace_service.service.OrderService;
import Vortex.marketplace_service.util.mappers.OrderDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceIMPL implements OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailsMapper orderDetailsMapper;

    @Override
    public List<OrderDetailsDTO> getAllOrdersByBuyerID(String buyerID) {
        List<Orders> ordersList = orderRepository.findAllByBuyerIDEquals(buyerID);
        return getOrderDetailsDTOS(ordersList);
    }

    private List<OrderDetailsDTO> getOrderDetailsDTOS(List<Orders> ordersList) {
        List<OrderDetailsDTO> orderDetailsDTOList = orderDetailsMapper.EntityListToDTOList(ordersList);
        for (OrderDetailsDTO orderDetailsDTO:orderDetailsDTOList){
            Optional<Item> byId = itemRepository.findById(orderDetailsDTO.getItemID());
            orderDetailsDTO.setItemName(byId.get().getItemName());
            orderDetailsDTO.setItemImageURL(byId.get().getItemImageURL());
            orderDetailsDTO.setItemPrice(byId.get().getPrice());
        }
        return orderDetailsDTOList;
    }

    @Override
    public List<OrderDetailsDTO> getAllOrdersBySellerID(String sellerID) {
        List<Orders> ordersList = orderRepository.findAllBySellerIDEquals(sellerID);
        return getOrderDetailsDTOS(ordersList);
    }
}
