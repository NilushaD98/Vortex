package Vortex.userservice.controller;

import Vortex.userservice.dto.request.FollowRequestDTO;
import Vortex.userservice.service.UserService;
import Vortex.userservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userservice/api/v1/user/")
public class UserController {

    private final UserService userService;
    @PostMapping("follow")
    public ResponseEntity<StandardResponse> follow(@RequestBody FollowRequestDTO followRequestDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"User Follow Status : ",userService.follow(followRequestDTO)),HttpStatus.OK
        );
    }
    @PatchMapping("unfollow")
    public ResponseEntity<StandardResponse> unfollow(@RequestBody FollowRequestDTO followRequestDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"User Unfollow Status : ",userService.unfollow(followRequestDTO)),HttpStatus.OK
        );
    }
    @GetMapping("get_following_list")
    public ResponseEntity<StandardResponse> getFollowingList(@RequestParam("userEmail")String userEmail){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Following List",userService.getFollowingList(userEmail)),HttpStatus.OK
        );
    }

}
