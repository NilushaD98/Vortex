package Vortex.postservice.controller;

import Vortex.postservice.service.PostService;
import Vortex.postservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/vortexpostservice/api/v1/admin/")
public class AdminController {

    private final PostService postService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("get_all_reported_posts")
    public ResponseEntity<StandardResponse> getAllReportedPosts(){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Reported Posts ;",postService.getAllReportedPosts()), HttpStatus.OK
        );
    }
    @DeleteMapping("removeuser")
    public Boolean removeUser(@RequestParam("userEmail")String userEmail){
        return postService.removeUserPosts(userEmail);
    }

}
