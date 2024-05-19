package Vortex.authservice.feign;

import Vortex.authservice.util.StandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service",url = "http://localhost:8200")
public interface UserServiceProxy {

    @PostMapping("/userservice/api/v1/user/initializeFollowingAndFollowersList")
    public ResponseEntity<StandardResponse> initializeFollowingAndFollowersLis(@RequestParam("userEmail")String userEmail);
}
