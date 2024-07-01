package Vortex.userservice.repository;

import Vortex.userservice.collection.Followers;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowersRepository extends MongoRepository<Followers,String> {

    Followers findByUserEmailEquals(String userEmail);

    void deleteByUserEmailEquals(String userEmail);
}

