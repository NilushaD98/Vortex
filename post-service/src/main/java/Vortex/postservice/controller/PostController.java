package Vortex.postservice.controller;

import Vortex.postservice.dto.request.AddCommentDTO;
import Vortex.postservice.dto.request.PostAddDTO;
import Vortex.postservice.dto.request.PostLikeDTO;
import Vortex.postservice.dto.request.SharePostDTO;
import Vortex.postservice.service.PostService;
import Vortex.postservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
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
    @PostMapping("add_comment")
    public ResponseEntity<StandardResponse> addComment(@RequestBody AddCommentDTO addCommentDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Comment Added Status ",postService.addComment(addCommentDTO)),HttpStatus.OK
        );
    }
    @DeleteMapping("delete_comment")
    public ResponseEntity<StandardResponse> deleteComment(@RequestBody AddCommentDTO addCommentDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Comment Delete Status ",postService.deleteComment(addCommentDTO)),HttpStatus.OK
        );
    }
    @GetMapping("get_all_comment_by_postid")
    public ResponseEntity<StandardResponse> getAllCommentsByPostID(
            @RequestParam("postID")String postID,
            @RequestParam("pageIndex")int pageIndex
    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Comments",postService.getAllComments(postID,pageIndex)),HttpStatus.OK
        );
    }
    @GetMapping("get_all_like_list_by_postID")
    public ResponseEntity<StandardResponse> getAllLikeList(
            @RequestParam("postID")String postID,
            @RequestParam("pageIndex")int pageIndex
    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Liked User List",postService.getAllLikelist(postID,pageIndex)),HttpStatus.OK
        );
    }
    @DeleteMapping("delete_post")
    public ResponseEntity<StandardResponse> deletePost(
            @RequestParam("postID")String postID,
            @RequestParam("authorEmail")String authorEmail
    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Post Delete Status :",postService.deletePost(postID,authorEmail)),HttpStatus.OK
        );
    }

}
