package Vortex.marketplace_service.repository;

import Vortex.marketplace_service.collection.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Orders,String> {
    List<Orders> findAllByBuyerIDEquals(String buyerID);
    List<Orders> findAllBySellerIDEquals(String buyerID);
}
