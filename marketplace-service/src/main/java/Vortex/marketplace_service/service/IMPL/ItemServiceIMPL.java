package Vortex.marketplace_service.service.IMPL;

import Vortex.marketplace_service.collection.Item;
import Vortex.marketplace_service.dto.request.AddItemDTO;
import Vortex.marketplace_service.enums.ItemStatus;
import Vortex.marketplace_service.exception.ItemUnavailableException;
import Vortex.marketplace_service.repository.ItemRepository;
import Vortex.marketplace_service.service.ItemService;
import Vortex.marketplace_service.util.mappers.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceIMPL implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public String generateId() {
        String uuid = UUID.randomUUID().toString();
        long number = Math.abs(uuid.hashCode());
        return String.format("%010d", number);
    }

    @Override
    public Boolean addItem(AddItemDTO addItemDTO) {
        try {
            Item item = new Item(
                    generateId(),
                    addItemDTO.getItemName(),
                    addItemDTO.getItemDescription(),
                    ItemStatus.DISABLED,
                    addItemDTO.getItemImageURL(),
                    addItemDTO.getPrice(),
                    addItemDTO.getSellerID(),
                    addItemDTO.getQuantity()
            );
            itemRepository.save(item);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean publishItem(String itemID) {
        try {
            Optional<Item> byId = itemRepository.findById(itemID);
            byId.get().setItemStatus(ItemStatus.ACTIVE);
            itemRepository.save(byId.get());
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean updateItem(AddItemDTO addItemDTO) {
        Optional<Item> byId = itemRepository.findById(addItemDTO.getItemID());
        try {
            Item item = byId.get();
            item.setItemName(addItemDTO.getItemName());
            item.setItemDescription(addItemDTO.getItemDescription());
            item.setItemStatus(addItemDTO.getItemStatus());
            item.setItemImageURL(addItemDTO.getItemImageURL());
            item.setPrice(addItemDTO.getPrice());
            item.setQuantity(addItemDTO.getQuantity());
            itemRepository.save(item);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public List<AddItemDTO> getAllActiveItems() {
        List<Item> itemList = itemRepository.getAllByItemStatusEquals(ItemStatus.ACTIVE);
        return itemMapper.entityListToDTOList(itemList);
    }

    @Override
    public List<AddItemDTO> getAllItemBySellerID(String sellerID) {
        List<Item> itemList = itemRepository.getAllBySellerIDEquals(sellerID);
        return itemMapper.entityListToDTOList(itemList);
    }

    @Override
    public List<AddItemDTO> getAllUnPublishItems() {

        List<Item> itemList = itemRepository.getAllByItemStatusEquals(ItemStatus.DISABLED);
        return itemMapper.entityListToDTOList(itemList);
    }

    @Override
    public AddItemDTO getItemById(String itemID) {

        Optional<Item> byId = itemRepository.findById(itemID);
        if(byId.isPresent()){
            return itemMapper.entityToDTO(byId.get());
        }else {
            throw new ItemUnavailableException();
        }
    }
}
