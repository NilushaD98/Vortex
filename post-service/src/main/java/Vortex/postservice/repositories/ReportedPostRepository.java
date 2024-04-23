package Vortex.postservice.repositories;

import Vortex.postservice.collection.ReportedPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedPostRepository extends MongoRepository<ReportedPost,String> {
}
