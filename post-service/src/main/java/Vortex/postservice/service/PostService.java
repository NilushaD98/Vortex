package Vortex.postservice.service;

import Vortex.postservice.dto.request.*;
import Vortex.postservice.dto.response.*;

import java.util.List;

public interface PostService {
    String addPost(PostAddDTO postAddDTO);
    String likePost(PostLikeDTO postLikeDTO);
    List<PostViewDTO> getAllPostsByUserEmail(String userProfileEmail, String viewedUserEmail, int page);
    Boolean sharePost(SharePostDTO sharePostDTO);
    List<AllPostViewDTO> getAllPosts(String userEmail, int postPageIndex);

    Boolean unlikePost(PostLikeDTO postLikeDTO);

    Boolean deletePost(String postID, String authorEmail);

    Boolean addComment(AddCommentDTO addCommentDTO);
    Boolean deleteComment(DeleteCommentDTO deleteCommentDTO);

    List<ViewCommentDTO> getAllComments(String postID);

    List<UserByEmailDTO> getAllLikelist(String postID);

    String reportPost(PostReportDTO postReportDTO);

    List<ReportedPostDTO> getAllReportedPosts();

    Boolean replyComment(ReplyCommentDTO replyCommentDTO);

    Boolean likeComment(LikeCommentDTO likeCommentDTO);

    Boolean likeReplyComment(LikeReplyCommentDTO likeReplyCommentDTO);

    List<ViewReplyCommentDTO> getAllReplyComments(String commentID);

    Boolean deleteReplyComment(String replyCommentId);

    List<AllPostViewDTO> getAllHomePagePosts(String userEmail, int postPageIndex);
}
