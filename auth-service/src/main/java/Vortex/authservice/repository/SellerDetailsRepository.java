package Vortex.authservice.repository;

import Vortex.authservice.entity.SellerDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerDetailsRepository extends MongoRepository<SellerDetails,String> {
}
