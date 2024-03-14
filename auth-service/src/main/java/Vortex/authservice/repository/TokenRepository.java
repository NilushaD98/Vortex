package Vortex.authservice.repository;

import Vortex.authservice.entity.Token;
import Vortex.authservice.entity.User;
import jdk.jshell.EvalException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token,String> {

    @Query(value = "{'token': ?0}")
    Optional<Token> findTokensByToken(String token);

    @Query(value = "{'userEmail': ?0}")
    List<Token> findTokensByUserEmailEquals(String email);
}
