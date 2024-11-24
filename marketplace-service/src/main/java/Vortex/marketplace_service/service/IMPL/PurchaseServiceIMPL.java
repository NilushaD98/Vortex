package Vortex.marketplace_service.service.IMPL;

import Vortex.marketplace_service.collection.Item;
import Vortex.marketplace_service.collection.Orders;
import Vortex.marketplace_service.dto.request.OrderDTO;
import Vortex.marketplace_service.dto.request.PaySecondPaymentDTO;
import Vortex.marketplace_service.dto.request.UpdateOrderStatusDTO;
import Vortex.marketplace_service.dto.response.OrderSuccessDTO;
import Vortex.marketplace_service.enums.OrderStatus;
import Vortex.marketplace_service.exception.ItemUnavailableException;
import Vortex.marketplace_service.feign.AuthServiceProxy;
import Vortex.marketplace_service.repository.ItemRepository;
import Vortex.marketplace_service.repository.OrderRepository;
import Vortex.marketplace_service.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PurchaseServiceIMPL implements PurchaseService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final AuthServiceProxy authServiceProxy;

    @Override
    public OrderSuccessDTO order(OrderDTO orderDTO) {
        Optional<Item> itemById = itemRepository.findById(orderDTO.getItemID());
        if(!itemById.isPresent() || (itemById.get().getQuantity()<orderDTO.getQuantity())){
            throw new ItemUnavailableException();
        }else{
            //transfer ethereum if not success then throw error name as payment unsuccessful;
            // send email to both seller and buyer
            //get seller wallet address from auth service
            Orders orders = new Orders(
                    idGenerator(),
                    orderDTO.getTransactionID(),
                    orderDTO.getItemID(),
                    orderDTO.getQuantity(),
                    orderDTO.getBuyerID(),
                    orderDTO.getSellerID(),
                    itemById.get().getPrice() * orderDTO.getQuantity(),
                    (itemById.get().getPrice() * orderDTO.getQuantity())/2,
                    orderDTO.getAddress(),
                    orderDTO.getBuyerWalletID(),
                    authServiceProxy.getSellerDetails(orderDTO.getSellerID()).getMetaMaskID(),
                    OrderStatus.PROCESSING
            );
            orderRepository.save(orders);
            Item item = itemRepository.findById(orderDTO.getItemID()).get();
            if(item.getQuantity()>= orders.getQuantity()){
                item.setQuantity(item.getQuantity()- orders.getQuantity());
                try {
                    itemRepository.save(item);
                }catch (Exception e){
                    log.error(e.getMessage());
                    throw new ItemUnavailableException();
                }
            }else {
                throw new ItemUnavailableException();
            }
            return new OrderSuccessDTO(true,orders.getOrderID());
        }

    }

    @Override
    public Boolean updateOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO) {
        try {
            Optional<Orders> byId = orderRepository.findById(updateOrderStatusDTO.getOrderID());
            byId.get().setOrderStatus(updateOrderStatusDTO.getOrderStatus());
            orderRepository.save(byId.get());
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean paySecondPayment(PaySecondPaymentDTO paySecondPaymentDTO) {

        try {
            Optional<Orders> byId = orderRepository.findById(paySecondPaymentDTO.getOrderID());
            //call blockchain and pay the second payment

            byId.get().setOrderStatus(OrderStatus.DELIVERED);
            byId.get().setPaidPrice(byId.get().getPrice());
            orderRepository.save(byId.get());
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    private String idGenerator(){
        Random random = new Random();
        long randomNumber = 100_000_000_000_000L + (long) (random.nextDouble() * 900_000_000_000_000L);
        return String.valueOf(randomNumber);
    }
}


//endpoints
//1. update order status - seller
//2.get all order details - seller/customer(include item data)
