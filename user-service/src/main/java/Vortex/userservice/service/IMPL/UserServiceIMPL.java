package Vortex.userservice.service.IMPL;
import Vortex.userservice.dto.response.FollowersAndFollowingCountDTO;
import Vortex.userservice.dto.response.FollowingDTO;
import Vortex.userservice.feign.AuthServiceProxy;
import Vortex.userservice.collection.Followers;
import Vortex.userservice.collection.Following;
import Vortex.userservice.dto.request.FollowRequestDTO;
import Vortex.userservice.dto.request.FollowerDetailsDTO;
import Vortex.userservice.repository.FollowersRepository;
import Vortex.userservice.repository.FollowingRepository;
import Vortex.userservice.service.UserService;
import Vortex.userservice.util.mappers.UserMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceIMPL implements UserService{

    @Autowired
    private FollowersRepository followersRepository;
    @Autowired
    private FollowingRepository followingRepository;
    @Autowired
    private AuthServiceProxy authServiceProxy;
    private final MongoDatabase mongoDatabase;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private UserMapper userMapper;
    @Value("${spring.data.mongodb.uri}")
    private String mongoClientURL;


    public  UserServiceIMPL() {
        MongoClient mongoClient = MongoClients.create(mongoClientURL);
        mongoDatabase = mongoClient.getDatabase("user");
    }

    @Override
    public String follow(FollowRequestDTO followRequestDTO) {
        if(databaseUpdate(followRequestDTO,"$addToSet")){
            return "success";
        }else {
            return "unsuccessful";
        }
    }
    @Override
    public String unfollow(FollowRequestDTO followRequestDTO) {
        if(databaseUpdate(followRequestDTO,"$pull")){
            return "unfollowed";
        }else {
            return "unfollow failed";
        }
    }
    private Boolean databaseUpdate(FollowRequestDTO followRequestDTO,String followUnfollowStatus){
        try{
            MongoCollection<Document> followingCollection = mongoDatabase.getCollection("following");
            MongoCollection<Document> followersCollection = mongoDatabase.getCollection("followers");
            UpdateResult updateResult = followingCollection.updateOne(
                    new Document("userEmail", followRequestDTO.getEmail()),
                    new Document(followUnfollowStatus, new Document("followingUserEmailList", followRequestDTO.getFollowingEmail())));
            log.info(updateResult.toString());
            UpdateResult updateResult1 = followersCollection.updateOne(
                    new Document("userEmail", followRequestDTO.getFollowingEmail()),
                    new Document(followUnfollowStatus, new Document("followersEmailList", followRequestDTO.getEmail()))
            );
            log.info(updateResult1.toString());
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    @Override
    public List<FollowerDetailsDTO> getFollowingList(String userEmail) {
        Following byUserEmailEquals = followingRepository.findByUserEmailEquals(userEmail);
        List<String> followingUserEmailList = byUserEmailEquals.getFollowingUserEmailList();
        if(followingUserEmailList.isEmpty()){
            return null;
        }else {
            List<FollowerDetailsDTO> followerDetailsDTOList = authServiceProxy.getFollowingDataList(followingUserEmailList);
            return followerDetailsDTOList;
        }
    }
    @Override
    public List<FollowerDetailsDTO> getFollowersList(String userEmail) {
        Followers byUserEmailEquals = followersRepository.findByUserEmailEquals(userEmail);
        List<String> followersEmailList = byUserEmailEquals.getFollowersEmailList();
        if(followersEmailList.isEmpty()){
            return null;
        }else {
            List<FollowerDetailsDTO> followerDetailsDTOList = authServiceProxy.getFollowersDataList(followersEmailList);
            return followerDetailsDTOList;
        }
    }

    @Override
    public FollowersAndFollowingCountDTO getFollowersAndFollowingCount(String email) {
        AggregationOperation match = Aggregation.match(Criteria.where("userEmail").is(email));
        AggregationOperation project = Aggregation.project().and("followingUserEmailList").size().as("size");
        AggregationOperation project1 = Aggregation.project().and("followersEmailList").size().as("size");
        Aggregation aggregation = Aggregation.newAggregation(match, project);
        Aggregation aggregation2 = Aggregation.newAggregation(match, project1);
        Document result = mongoOperations.aggregate(aggregation, "following", Document.class).getUniqueMappedResult();
        Document result1 = mongoOperations.aggregate(aggregation2, "followers", Document.class).getUniqueMappedResult();
        FollowersAndFollowingCountDTO followersAndFollowingCountDTO = new FollowersAndFollowingCountDTO();
        log.info(result.toString());
        if(result == null){
            followersAndFollowingCountDTO.setFollowingCount(0);
        }else {
            followersAndFollowingCountDTO.setFollowingCount(result.getInteger("size"));
        }
        if(result1 == null){
            followersAndFollowingCountDTO.setFollowersCount(0);
        }else {
            followersAndFollowingCountDTO.setFollowersCount(result1.getInteger("size"));
        }

        return followersAndFollowingCountDTO;
    }

    @Override
    public FollowingDTO getFollowingListForPostService(String email) {
        Following following = followingRepository.findByUserEmailEquals(email);
        FollowingDTO followingDTO  = userMapper.FollowingEntityToDTO(following);
        return followingDTO;
    }
}

