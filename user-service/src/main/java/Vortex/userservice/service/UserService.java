package Vortex.userservice.service;

import Vortex.userservice.dto.request.FollowRequestDTO;
import Vortex.userservice.dto.request.FollowerDetailsDTO;

import java.util.List;

public interface UserService {
    String follow(FollowRequestDTO followRequestDTO);

    String unfollow(FollowRequestDTO followRequestDTO);

    List<FollowerDetailsDTO> getFollowingList(String userEmail);

    List<FollowerDetailsDTO> getFollowersList(String userEmail);
}
