package Vortex.marketplace_service.service;

import Vortex.marketplace_service.dto.request.AddItemDTO;

import java.util.List;

public interface ItemService {
    Boolean addItem(AddItemDTO addItemDTO);

    Boolean publishItem(String itemID);

    Boolean updateItem(AddItemDTO addItemDTO);

    List<AddItemDTO> getAllActiveItems();

    List<AddItemDTO> getAllItemBySellerID(String sellerID);

    List<AddItemDTO> getAllUnPublishItems();
}
