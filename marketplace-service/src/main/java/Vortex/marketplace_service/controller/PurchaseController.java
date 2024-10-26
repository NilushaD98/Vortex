package Vortex.marketplace_service.controller;

import Vortex.marketplace_service.dto.request.OrderDTO;
import Vortex.marketplace_service.dto.request.PaySecondPaymentDTO;
import Vortex.marketplace_service.dto.request.UpdateOrderStatusDTO;
import Vortex.marketplace_service.service.PurchaseService;
import Vortex.marketplace_service.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/marketplaceservice/api/v1/purchase/")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("order")
    public ResponseEntity<StandardResponse> order(@RequestBody OrderDTO orderDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Order Placed Status : ",purchaseService.order(orderDTO)),HttpStatus.OK
        );
    }
    @PutMapping("updateOrderStatus")//seller
    public ResponseEntity<StandardResponse> updateOrderStatus(@RequestBody UpdateOrderStatusDTO updateOrderStatusDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Order Status Update Status:",purchaseService.updateOrderStatus(updateOrderStatusDTO)),HttpStatus.OK
        );
    }
    @PostMapping("paySecondPayment")
    public ResponseEntity<StandardResponse> paySecondPayment(@RequestBody PaySecondPaymentDTO paySecondPaymentDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Second Payment Success Status:",purchaseService.paySecondPayment(paySecondPaymentDTO)),HttpStatus.OK
        );
    }
}
