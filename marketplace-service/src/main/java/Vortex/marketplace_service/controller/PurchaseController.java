package Vortex.marketplace_service.controller;

import Vortex.marketplace_service.dto.request.OrderDTO;
import Vortex.marketplace_service.service.PurchaseService;
import Vortex.marketplace_service.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
