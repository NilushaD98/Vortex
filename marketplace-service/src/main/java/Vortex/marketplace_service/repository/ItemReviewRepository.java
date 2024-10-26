package Vortex.marketplace_service.repository;

import Vortex.marketplace_service.collection.ItemReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemReviewRepository extends MongoRepository<ItemReview,String> {
    Optional<ItemReview> findByOrderIDEquals(String orderID);

    List<ItemReview> findByItemIDEquals(String itemID);
}
