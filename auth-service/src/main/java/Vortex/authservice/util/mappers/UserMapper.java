package Vortex.authservice.util.mappers;

import Vortex.authservice.dto.request.FollowerDetailsDTO;
import Vortex.authservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "role.name", target = "role") // Map the role field
    List<FollowerDetailsDTO> EntityTOFollowerDetailsDTO(List<User> byEmailIn);
}
