package Vortex.marketplace_service.service.IMPL;

import Vortex.marketplace_service.collection.Item;
import Vortex.marketplace_service.collection.ItemReview;
import Vortex.marketplace_service.collection.Orders;
import Vortex.marketplace_service.dto.request.RateOrderDTO;
import Vortex.marketplace_service.dto.response.OrderDetailsDTO;
import Vortex.marketplace_service.repository.ItemRepository;
import Vortex.marketplace_service.repository.ItemReviewRepository;
import Vortex.marketplace_service.repository.OrderRepository;
import Vortex.marketplace_service.service.OrderService;
import Vortex.marketplace_service.util.mappers.OrderDetailsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceIMPL implements OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailsMapper orderDetailsMapper;
    private final ItemReviewRepository itemReviewRepository;

    @Override
    public List<OrderDetailsDTO> getAllOrdersByBuyerID(String buyerID) {
        List<Orders> ordersList = orderRepository.findAllByBuyerIDEquals(buyerID);
        return getOrderDetailsDTOS(ordersList);
    }

    private List<OrderDetailsDTO> getOrderDetailsDTOS(List<Orders> ordersList) {
        List<OrderDetailsDTO> orderDetailsDTOList = orderDetailsMapper.EntityListToDTOList(ordersList);
        for (OrderDetailsDTO orderDetailsDTO:orderDetailsDTOList){
            Optional<Item> byId = itemRepository.findById(orderDetailsDTO.getItemID());
            Optional<ItemReview> itemReview = itemReviewRepository.findByOrderIDEquals(orderDetailsDTO.getOrderID());
            log.info(""+itemReview.isPresent());
            orderDetailsDTO.setRate(itemReview.isPresent() == true ? itemReview.get().getRate():0);
            orderDetailsDTO.setRateMessage(itemReview.isPresent() == true ? itemReview.get().getRateMessage():"");
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

    @Override
    public Boolean rateOrder(RateOrderDTO rateOrderDTO) {
        try {
            Optional<Orders> byId = orderRepository.findById(rateOrderDTO.getOrderID());
            Optional<ItemReview> itemRevieww = itemReviewRepository.findByOrderIDEquals(rateOrderDTO.getOrderID());
            if(itemRevieww.isPresent()){
                itemRevieww.get().setRate(rateOrderDTO.getRate());
                itemRevieww.get().setRateMessage(rateOrderDTO.getRateMessage());
                itemReviewRepository.save(itemRevieww.get());

            }else {
                ItemReview itemReview = new ItemReview(
                        byId.get().getOrderID(),
                        byId.get().getItemID(),
                        rateOrderDTO.getRate(),
                        rateOrderDTO.getRateMessage()
                );
                itemReviewRepository.save(itemReview);

            }

            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
}
