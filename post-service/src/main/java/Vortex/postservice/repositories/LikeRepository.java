package Vortex.postservice.repositories;

import Vortex.postservice.collection.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends MongoRepository<Like,String> {
    Optional<Like> findLikeByPostIdEquals(String postID);
}
