package Vortex.userservice.service.IMPL;

import Vortex.userservice.collection.Following;
import Vortex.userservice.dto.request.FollowRequestDTO;
import Vortex.userservice.dto.request.FollowerDetailsDTO;
import Vortex.userservice.dto.response.FollowingListDTO;
import Vortex.userservice.feign.AuthServiceProxy;
import Vortex.userservice.repository.FollowersRepository;
import Vortex.userservice.repository.FollowingRepository;
import Vortex.userservice.service.UserService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceIMPL implements UserService {

    @Autowired
    private FollowersRepository followersRepository;
    @Autowired
    private FollowingRepository followingRepository;
    @Autowired
    private AuthServiceProxy authServiceProxy;
    private final MongoDatabase mongoDatabase;


    public  UserServiceIMPL() {
        MongoClient mongoClient = MongoClients.create("mongodb+srv://root:1234@cluster0.ucithrp.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
        mongoDatabase = mongoClient.getDatabase("user");
    }

    @Override
    public String follow(FollowRequestDTO followRequestDTO) {
        try {
            MongoCollection<Document> followingCollection = mongoDatabase.getCollection("following");
            MongoCollection<Document> followersCollection = mongoDatabase.getCollection("followers");
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult updateResult = followingCollection.updateOne(
                    new Document("userEmail", followRequestDTO.getEmail()),
                    new Document("$addToSet", new Document("followingUserEmailList", followRequestDTO.getFollowingEmail())),
                    options);
            log.info(updateResult.toString());
            UpdateResult updateResult1 = followersCollection.updateOne(
                    new Document("userEmail", followRequestDTO.getFollowingEmail()),
                    new Document("$addToSet", new Document("followersEmailList", followRequestDTO.getEmail())),
                    options
            );
            log.info(updateResult1.toString());
            return "success";
        }catch (Exception e){
            log.error(e.getMessage());
            return "unsuccessful";
        }
    }
    @Override
    public String unfollow(FollowRequestDTO followRequestDTO) {
        try {
            MongoCollection<Document> followingCollection = mongoDatabase.getCollection("following");
            MongoCollection<Document> followersCollection = mongoDatabase.getCollection("followers");
            UpdateResult updateResult = followingCollection.updateOne(
                    new Document("userEmail", followRequestDTO.getEmail()),
                    new Document("$pull", new Document("followingUserEmailList", followRequestDTO.getFollowingEmail())));
            log.info(updateResult.toString());
            UpdateResult updateResult1 = followersCollection.updateOne(
                    new Document("userEmail", followRequestDTO.getFollowingEmail()),
                    new Document("$pull", new Document("followersEmailList", followRequestDTO.getEmail()))
            );
            log.info(updateResult1.toString());
            return "unfollowed.";
        }catch (Exception e){
            log.error(e.getMessage());
            return "failed to unfollow";
        }
    }
    @Override
    public List<FollowingListDTO> getFollowingList(String userEmail) {
        Following byUserEmailEquals = followingRepository.findByUserEmailEquals(userEmail);
        List<String> followingUserEmailList = byUserEmailEquals.getFollowingUserEmailList();
        List<FollowerDetailsDTO> followerDetailsDTOList = authServiceProxy.getFollowersDataList(followingUserEmailList);
        return null;
    }
}
