package Vortex.postservice.repositories;

import Vortex.postservice.collection.SharedPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface SharedPostRepository extends MongoRepository<SharedPost,String> {
}
