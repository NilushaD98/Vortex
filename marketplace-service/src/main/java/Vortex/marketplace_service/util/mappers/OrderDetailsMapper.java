package Vortex.marketplace_service.util.mappers;

import Vortex.marketplace_service.collection.Orders;
import Vortex.marketplace_service.dto.response.OrderDetailsDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailsMapper {
    List<OrderDetailsDTO> EntityListToDTOList(List<Orders> ordersList);
}
