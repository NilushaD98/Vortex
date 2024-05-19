package Vortex.postservice.feign;

import Vortex.postservice.dto.request.UserByEmailDTO;
import Vortex.postservice.util.StandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "auth-service",url = "http://localhost:8100")
public interface AuthServiceProxy {
    @GetMapping("/vortexcoreservice/api/v1/user/find_account_by_email")
    public ResponseEntity<StandardResponse> findAccountById(@RequestParam("email")String email);
    @PostMapping("/vortexcoreservice/api/v1/user/get_following_data_list")
    List<UserByEmailDTO> getFollowingDataList(List<String> followingUserEmailList);

    @GetMapping("/vortexcoreservice/api/v1/user/get_user")
    public UserByEmailDTO getUser(@RequestParam("user_id") String user_id);
}
