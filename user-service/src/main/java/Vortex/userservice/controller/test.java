package Vortex.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class test {

    @GetMapping("testget")
    public String testMethod(){
        return "test pass";
    }
}
