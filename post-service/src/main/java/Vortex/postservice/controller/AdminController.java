package Vortex.postservice.controller;

import Vortex.postservice.service.PostService;
import Vortex.postservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
