package Vortex.userservice.service;

import Vortex.userservice.dto.request.FollowRequestDTO;
import Vortex.userservice.dto.request.FollowerDetailsDTO;
import Vortex.userservice.dto.response.FollowersAndFollowingCountDTO;
import Vortex.userservice.dto.response.FollowingDTO;

import java.util.List;

public interface UserService {
    String follow(FollowRequestDTO followRequestDTO);

    String unfollow(FollowRequestDTO followRequestDTO);

    List<FollowerDetailsDTO> getFollowingList(String userEmail);

    List<FollowerDetailsDTO> getFollowersList(String userEmail);

    public FollowersAndFollowingCountDTO getFollowersAndFollowingCount(String email);

    FollowingDTO getFollowingListForPostService(String email);
}
