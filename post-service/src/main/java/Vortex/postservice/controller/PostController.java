package Vortex.postservice.controller;

import Vortex.postservice.dto.request.PostAddDTO;
import Vortex.postservice.dto.request.PostLikeDTO;
import Vortex.postservice.dto.request.SharePostDTO;
import Vortex.postservice.service.PostService;
import Vortex.postservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vortexpostservice/api/v1/posts/")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("add_post")
    public ResponseEntity<StandardResponse> addPost(@RequestBody PostAddDTO postAddDTO){
        System.out.println(postAddDTO);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Post Add Status : ",postService.addPost(postAddDTO)), HttpStatus.OK
        );
    }
    @PostMapping("like_post")
    public ResponseEntity<StandardResponse> likePost(@RequestBody PostLikeDTO postLikeDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Liked Status",postService.likePost(postLikeDTO)),HttpStatus.OK
        );
    }
    @PostMapping("unlike_post")
    public ResponseEntity<StandardResponse> unlikePost(@RequestBody PostLikeDTO postLikeDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Unlike Status ",postService.unlikePost(postLikeDTO)),HttpStatus.OK
        );
    }
    @GetMapping("get_users_posts")
    public ResponseEntity<StandardResponse> getUserPosts(
            @RequestParam("userProfileEmail")String userProfileEmail,
            @RequestParam("viewedUserEmail")String viewedUserEmail,
            @RequestParam("page")int page
            )
    {
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(
                        200,
                        userProfileEmail + "'s all posts",
                        postService.getAllPostsByUserEmail(userProfileEmail,viewedUserEmail,page)
                ),HttpStatus.OK
        );
    }
    @PostMapping("share_post")
    public ResponseEntity<StandardResponse> sharePost(@RequestBody SharePostDTO sharePostDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Shared Status",postService.sharePost(sharePostDTO)),HttpStatus.OK
        );
    }
    @GetMapping("get_all_follow_user_posts")
    public ResponseEntity<StandardResponse> getAllFollowUserPosts(
            @RequestParam("userEmail")String userEmail,
            @RequestParam("postPageIndex")int postPageIndex

    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All posts ",postService.getAllPosts(userEmail,postPageIndex)),HttpStatus.OK
        );
    }

}
