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

    List<ViewCommentDTO> getAllComments(String postID, int pageIndex);

    List<UserByEmailDTO> getAllLikelist(String postID, int pageIndex);

    String reportPost(PostReportDTO postReportDTO);

    List<ReportedPostDTO> getAllReportedPosts();
}
