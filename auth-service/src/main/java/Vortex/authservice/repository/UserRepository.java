package Vortex.authservice.repository;

import Vortex.authservice.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableMongoRepositories
public interface UserRepository extends MongoRepository<User,String> {

    @Query(value = "{'email': ?0}")
    Optional<User> findByEmailEquals(String email);

    List<User> findByEmailIn(List<String> followingUserEmailList);

    @Query("{ 'firstName': { $regex: ?0, $options: 'i' } }")
    List<User> findUserByFirstNamePattern(String username);

    @Query("{ 'lastName': { $regex: ?0, $options: 'i' } }")
    List<User> findUserByLastNamePattern(String username);

}
