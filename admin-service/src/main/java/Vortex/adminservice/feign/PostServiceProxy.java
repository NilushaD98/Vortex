package Vortex.adminservice.feign;

import Vortex.adminservice.util.StandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "post-service",url = "http://localhost:8300")
public interface PostServiceProxy {

    @PostMapping("/vortexpostservice/api/v1/admin/get_all_reported_posts")
    public ResponseEntity<StandardResponse> getAllReportedPosts();

}
