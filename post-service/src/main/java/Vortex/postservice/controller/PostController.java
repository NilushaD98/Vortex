package Vortex.postservice.controller;

import Vortex.postservice.dto.request.*;
import Vortex.postservice.service.PostService;
import Vortex.postservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vortexpostservice/api/v1/posts/")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("add_post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> addPost(@RequestBody PostAddDTO postAddDTO){
        System.out.println(postAddDTO);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Post Add Status : ",postService.addPost(postAddDTO)), HttpStatus.OK
        );
    }
    @PostMapping("like_post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> likePost(@RequestBody PostLikeDTO postLikeDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Liked Status",postService.likePost(postLikeDTO)),HttpStatus.OK
        );
    }
    @PostMapping("unlike_post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> unlikePost(@RequestBody PostLikeDTO postLikeDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Unlike Status ",postService.unlikePost(postLikeDTO)),HttpStatus.OK
        );
    }
    @GetMapping("get_users_posts")
    @PreAuthorize("hasRole('USER')")
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> sharePost(@RequestBody SharePostDTO sharePostDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Shared Status",postService.sharePost(sharePostDTO)),HttpStatus.OK
        );
    }
    @GetMapping("get_all_follow_user_posts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getAllFollowUserPosts(
            @RequestParam("userEmail")String userEmail,
            @RequestParam("postPageIndex")int postPageIndex

    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All posts ",postService.getAllPosts(userEmail,postPageIndex)),HttpStatus.OK
        );
    }
    @GetMapping("getAllPosts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getAllPosts(
            @RequestParam("userEmail")String userEmail,
            @RequestParam("postPageIndex")int postPageIndex
    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Homepage Posts",postService.getAllHomePagePosts(userEmail,postPageIndex)),HttpStatus.OK
        );
    }
    @PostMapping("add_comment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> addComment(@RequestBody AddCommentDTO addCommentDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Comment Added Status ",postService.addComment(addCommentDTO)),HttpStatus.OK
        );
    }
    @DeleteMapping("delete_comment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> deleteComment(@RequestBody DeleteCommentDTO deleteCommentDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Comment Delete Status ",postService.deleteComment(deleteCommentDTO)),HttpStatus.OK
        );
    }
    @GetMapping("get_all_comment_by_postid")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getAllCommentsByPostID(
            @RequestParam("postID")String postID
    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Comments",postService.getAllComments(postID)),HttpStatus.OK
        );
    }
    @GetMapping("get_all_like_list_by_postID")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getAllLikeList(
            @RequestParam("postID")String postID
    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Liked User List",postService.getAllLikelist(postID)),HttpStatus.OK
        );
    }
    @DeleteMapping("delete_post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> deletePost(
            @RequestParam("postID")String postID,
            @RequestParam("authorEmail")String authorEmail
    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Post Delete Status :",postService.deletePost(postID,authorEmail)),HttpStatus.OK
        );
    }
    @PostMapping("reportPost")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> reportPost(@RequestBody PostReportDTO postReportDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Reported Status: ",postService.reportPost(postReportDTO)),HttpStatus.OK
        );
    }
    @PostMapping("replyComment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> replyComment(@RequestBody ReplyCommentDTO replyCommentDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Comment Reply Status",postService.replyComment(replyCommentDTO)),HttpStatus.OK
        );
    }
    @PostMapping("likeComment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> likeComment(@RequestBody LikeCommentDTO likeCommentDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Liked Status",postService.likeComment(likeCommentDTO)),HttpStatus.OK
        );
    }
    @PostMapping("likeReplyComment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> likeReplyComment(@RequestBody LikeReplyCommentDTO likeReplyCommentDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Liked Status",postService.likeReplyComment(likeReplyCommentDTO)),HttpStatus.OK
        );
    }
    @GetMapping("getAllReplyComments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> getAllReplyComments(@RequestParam("commentID")String commentID){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"All Reply Comments",postService.getAllReplyComments(commentID)),HttpStatus.OK
        );
    }
    @DeleteMapping("deleteReplyComment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> deleteReplyComment(@RequestParam("replyCommentID")String replyCommentId){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Reply Comment Deleted Status",postService.deleteReplyComment(replyCommentId)),HttpStatus.OK
        );
    }

}
