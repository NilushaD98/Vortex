package Vortex.postservice.repositories;

import Vortex.postservice.collection.Comment;
import Vortex.postservice.collection.Comments;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<Comments,String> {
    Optional<Comments> findByPostIdEquals(String postID);


    void deleteByPostIdEquals(String postID);
}
