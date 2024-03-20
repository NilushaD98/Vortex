package Vortex.userservice.controller;

import Vortex.userservice.service.UserService;
import Vortex.userservice.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vortexuserservicefeign/")
public class FeignController {

    @Autowired
    private UserService userService;

    @GetMapping("getFollowingListForPostService")
    public ResponseEntity<StandardResponse> getFollowingListForPostService(
            @RequestParam("email")String email
    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"FollowingDTO List For Post Service",userService.getFollowingListForPostService(email)), HttpStatus.OK
        );

    }
}
