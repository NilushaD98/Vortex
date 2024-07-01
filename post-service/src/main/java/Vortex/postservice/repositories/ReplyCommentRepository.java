package Vortex.postservice.repositories;

import Vortex.postservice.collection.ReplyComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyCommentRepository extends MongoRepository<ReplyComment,String> {
    List<ReplyComment> findByRepliedCommentIDIn(List<String> replyCommentsIDList);

    void deleteAllByRepliedUserEmailEquals(String userEmail);
}
