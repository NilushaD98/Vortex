package Vortex.marketplace_service.feign;

import Vortex.marketplace_service.dto.request.UserByEmailDTO;
import Vortex.marketplace_service.dto.response.SellerDetailsDTO;
import Vortex.marketplace_service.util.StandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service",url = "http://localhost:8100")
public interface AuthServiceProxy {

    @GetMapping("/vortexcoreservice/api/v1/user/getSellerDetails")
    public SellerDetailsDTO getSellerDetails(@RequestParam("sellerID")String sellerID);

    @GetMapping("/vortexcoreservice/api/v1/user/get_user")
    public UserByEmailDTO getUser(@RequestParam("user_id") String user_id);
}
