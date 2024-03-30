package Vortex.userservice.feign;

import Vortex.userservice.dto.request.FollowerDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "auth-service",url = "http://localhost:8100")
public interface AuthServiceProxy {
    @PostMapping("/vortexcoreservice/api/v1/user/get_following_data_list")
    List<FollowerDetailsDTO> getFollowingDataList(List<String> followingUserEmailList);

    @PostMapping("/vortexcoreservice/api/v1/user/get_followers_data_list")
    List<FollowerDetailsDTO> getFollowersDataList(List<String> followersEmailList);
}
