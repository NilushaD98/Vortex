package Vortex.userservice.repository;

import Vortex.userservice.collection.Following;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowingRepository extends MongoRepository<Following,String> {

    Following findByUserEmailEquals(String userEmail);

}
