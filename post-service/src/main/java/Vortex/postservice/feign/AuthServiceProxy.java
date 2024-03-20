package Vortex.postservice.feign;

import Vortex.postservice.util.StandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service",url = "http://localhost:8100")
public interface AuthServiceProxy {
    @GetMapping("/vortexcoreservice/api/v1/user/find_account_by_email")
    public ResponseEntity<StandardResponse> findAccountById(@RequestParam("email")String email);
}
