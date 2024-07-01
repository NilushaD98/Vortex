package Vortex.authservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "post-service",url = "http://localhost:8300")
public interface PostServiceProxy {

    @DeleteMapping("/vortexpostservice/api/v1/admin/removeuser")
    public Boolean removeUser(@RequestParam("userEmail")String userEmail);
}
