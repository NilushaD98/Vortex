package Vortex.userservice.service;

import Vortex.userservice.dto.request.FollowRequestDTO;
import Vortex.userservice.dto.response.FollowingListDTO;

import java.util.List;

public interface UserService {
    String follow(FollowRequestDTO followRequestDTO);

    String unfollow(FollowRequestDTO followRequestDTO);

    List<FollowingListDTO> getFollowingList(String userEmail);
}
