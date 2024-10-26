package Vortex.marketplace_service.controller;

import Vortex.marketplace_service.service.OrderService;
import Vortex.marketplace_service.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/marketplaceservice/api/v1/purchase/")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("getAllOrdersByBuyerID")
    public ResponseEntity<StandardResponse> getAllOrdersByBuyerID(@RequestParam("buyerID")String buyerID){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Orders By Buyer ID: ",orderService.getAllOrdersByBuyerID(buyerID)), HttpStatus.OK
        );
    }

    @GetMapping("getAllOrdersBySellerID")
    public ResponseEntity<StandardResponse> getAllOrdersBySellerID(@RequestParam("sellerID")String sellerID){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Orders By Seller ID: ",orderService.getAllOrdersBySellerID(sellerID)),HttpStatus.OK
        );
    }
}
