package Vortex.postservice.service;

import Vortex.postservice.dto.request.*;
import Vortex.postservice.dto.response.AllPostViewDTO;
import Vortex.postservice.dto.response.LikedUserViewDTO;
import Vortex.postservice.dto.response.ViewCommentDTO;
import Vortex.postservice.dto.response.PostViewDTO;

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
    Boolean deleteComment(AddCommentDTO addCommentDTO);

    List<ViewCommentDTO> getAllComments(String postID, int pageIndex);

    List<UserByEmailDTO> getAllLikelist(String postID, int pageIndex);
}
