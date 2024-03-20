package Vortex.postservice.repositories;

import Vortex.postservice.collection.LikedLog;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedLogRepository extends MongoRepository<LikedLog,String> {
    LikedLog findLikedLogByAuthorEmailEqualsAndLikedUserEmailEquals(String userProfileEmail, String viewedUserEmail);
}
