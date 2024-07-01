package Vortex.postservice.repositories;

import Vortex.postservice.collection.Post;
import org.apache.catalina.WebResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post,String> {
    Page<Post> findPostsByPostAuthorEmailEquals(String userProfileEmail, Pageable pageable);

    Page<Post> findPostsByPostAuthorEmailIn(List<String> followingUserEmailList, Pageable pageablePost);

    void deleteAllByPostAuthorEmailEquals(String userEmail);
}
