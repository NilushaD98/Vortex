package Vortex.marketplace_service.util.mappers;

import Vortex.marketplace_service.collection.Item;
import Vortex.marketplace_service.dto.request.AddItemDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    List<AddItemDTO> entityListToDTOList(List<Item> itemList);
}
