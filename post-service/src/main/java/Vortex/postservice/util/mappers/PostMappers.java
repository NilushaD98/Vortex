package Vortex.postservice.util.mappers;

import Vortex.postservice.collection.Post;
import Vortex.postservice.collection.ReportedPost;
import Vortex.postservice.collection.SharedPost;
import Vortex.postservice.dto.request.SharePostDTO;
import Vortex.postservice.dto.response.AllPostViewDTO;
import Vortex.postservice.dto.response.PostViewDTO;
import Vortex.postservice.dto.response.ReportedPostDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMappers {
    public List<PostViewDTO> pageToPostViewList(Page<Post> postPage);

    SharedPost sharedPostDtoToSharedPostEntity(SharePostDTO sharePostDTO);

    List<AllPostViewDTO> PageToDtoList(Page<Post> postPage);

    AllPostViewDTO postToAllPostViewDTO(Post post1);

}
