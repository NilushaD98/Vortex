package Vortex.marketplace_service.repository;

import Vortex.marketplace_service.collection.Item;
import Vortex.marketplace_service.enums.ItemStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item,String> {
    List<Item> getAllByItemStatusEquals(ItemStatus itemStatus);

    List<Item> getAllBySellerIDEquals(String sellerID);

//    List<Item> getAllByItemStatusEquals(ItemStatus itemStatus);
}
