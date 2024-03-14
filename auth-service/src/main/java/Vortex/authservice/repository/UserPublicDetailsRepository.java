package Vortex.authservice.repository;

import Vortex.authservice.entity.UserPublicDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPublicDetailsRepository extends MongoRepository<UserPublicDetails,String> {

    @Query(value = "{'userId' : ?0}")
    UserPublicDetails findByUserId(String userid);
}
