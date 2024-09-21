package Vortex.notificationservice.feign;

import Vortex.notificationservice.dto.Items;
import Vortex.notificationservice.dto.Rec;
import Vortex.notificationservice.dto.Recipie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "recipei",url = "http://127.0.0.1:5000")
public interface AIfeign {

    @PostMapping("/generate_recipes")
    public Recipie test(@RequestBody Items items);
}
