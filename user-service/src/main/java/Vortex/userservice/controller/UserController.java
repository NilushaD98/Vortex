package Vortex.userservice.controller;

import Vortex.userservice.dto.request.FollowRequestDTO;
import Vortex.userservice.service.UserService;
import Vortex.userservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userservice/api/v1/user/")
public class UserController {

    private final UserService userService;
    @PostMapping("follow")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> follow(@RequestBody FollowRequestDTO followRequestDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"User Follow Status : ",userService.follow(followRequestDTO)),HttpStatus.OK
        );
    }
    @PatchMapping("unfollow")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> unfollow(@RequestBody FollowRequestDTO followRequestDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"User Unfollow Status : ",userService.unfollow(followRequestDTO)),HttpStatus.OK
        );
    }
    @GetMapping("get_following_list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getFollowingList(
            @RequestParam("userEmail")String userEmail
    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"FollowingDTO List",userService.getFollowingList(userEmail)),HttpStatus.OK
        );
    }
    @GetMapping("get_followers_list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getFollowersList(
            @RequestParam("userEmail")String userEmail

    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Follower List",userService.getFollowersList(userEmail)),HttpStatus.OK
        );
    }
    @GetMapping("get_following_and_followers_user_count")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getFollowingAndFollowersUsersCount(@RequestParam("email")String email){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"FollowingDTO and Followers List Sizes : ", userService.getFollowersAndFollowingCount(email)),HttpStatus.OK
        );
    }

}
