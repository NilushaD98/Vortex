package Vortex.postservice.feign;

import Vortex.postservice.util.StandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service",url = "http://localhost:8200")
public interface UserServiceProxy {

    @PostMapping("vortexuserservicefeign/getFollowingListForPostService")
    public ResponseEntity<StandardResponse> getFollowingListForPostService(
            @RequestParam("email")String email
    );
}
