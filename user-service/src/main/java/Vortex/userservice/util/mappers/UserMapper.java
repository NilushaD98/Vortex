package Vortex.userservice.util.mappers;

import Vortex.userservice.collection.Following;
import Vortex.userservice.dto.response.FollowingDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    FollowingDTO FollowingEntityToDTO(Following following);
}
