package Vortex.marketplace_service.controller;

import Vortex.marketplace_service.dto.request.AddItemDTO;
import Vortex.marketplace_service.service.ItemService;
import Vortex.marketplace_service.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/marketplaceservice/api/v1/item/")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("addItem")
    public ResponseEntity<StandardResponse> addItem(@RequestBody AddItemDTO addItemDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(201,"Item Add Status : ",itemService.addItem(addItemDTO)),HttpStatus.CREATED
        );
    }
    @PutMapping("publishItem")
    public ResponseEntity<StandardResponse> publishItem(@RequestParam("itemID") String itemID){
        return new ResponseEntity<StandardResponse>(
             new StandardResponse(200,"Item Publish Status : ",itemService.publishItem(itemID)),HttpStatus.OK
        );
    }
    @PutMapping("updateItem")
    public ResponseEntity<StandardResponse> updateItem(@RequestBody AddItemDTO addItemDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Item Update Status",itemService.updateItem(addItemDTO)),HttpStatus.OK
        );
    }
    @GetMapping("getAllActiveItems")
    public ResponseEntity<StandardResponse> getAllActiveItems(){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Items",itemService.getAllActiveItems()),HttpStatus.OK
        );
    }

    @GetMapping("getAllItemBySellerID")
    public ResponseEntity<StandardResponse> getAllItemBySellerID(@RequestParam("sellerID") String sellerID){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Seller's Item : ",itemService.getAllItemBySellerID(sellerID)),HttpStatus.OK
        );
    }
    @GetMapping("getAllUnPublishItems")
    public ResponseEntity<StandardResponse> getAllUnPublishItems(){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"",itemService.getAllUnPublishItems()),HttpStatus.OK
        );
    }

    @GetMapping("getItemById")
    public ResponseEntity<StandardResponse> getItemById(@RequestParam("itemID")String itemID){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Item By ID",itemService.getItemById(itemID)),HttpStatus.OK
        );
    }
    @GetMapping("getRatingsByItemID")
    public ResponseEntity<StandardResponse> getRatingsByItemID(@RequestParam("itemID")String itemID){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Item Reviews : ",itemService.getRatingsByItemID(itemID)),HttpStatus.OK
        );
    }
}
