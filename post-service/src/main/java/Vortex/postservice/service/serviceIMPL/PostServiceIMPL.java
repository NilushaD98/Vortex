package Vortex.postservice.service.serviceIMPL;

import Vortex.postservice.collection.*;
import Vortex.postservice.dto.request.*;
import Vortex.postservice.dto.response.AllPostViewDTO;
import Vortex.postservice.dto.response.LikedUserViewDTO;
import Vortex.postservice.dto.response.PostViewDTO;
import Vortex.postservice.dto.response.ViewCommentDTO;
import Vortex.postservice.exception.CommentNotFoundException;
import Vortex.postservice.exception.PostNotFoundException;
import Vortex.postservice.feign.AuthServiceProxy;
import Vortex.postservice.feign.UserServiceProxy;
import Vortex.postservice.repositories.*;
import Vortex.postservice.service.PostService;
import Vortex.postservice.util.mappers.PostMappers;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceIMPL implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private LikedLogRepository likedLogRepository;
    @Autowired
    private PostMappers postMappers;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SharedPostRepository sharedPostRepository;
    @Autowired
    private AuthServiceProxy authServiceProxy;
    private final MongoDatabase mongoDatabase;
    private final MongoDatabase userDataBase;
    private final MongoDatabase vortexDataBase;

    public PostServiceIMPL(){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://root:1234@cluster0.ucithrp.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
        mongoDatabase = mongoClient.getDatabase("post");
        userDataBase = mongoClient.getDatabase("user");
        vortexDataBase = mongoClient.getDatabase("vortex");
    }
    @Override
    public String addPost(PostAddDTO postAddDTO) {
        Post post = new Post(
                postAddDTO.getPostAuthorEmail(),
                postAddDTO.getMediaLink(),
                postAddDTO.getCaption(),
                postAddDTO.getPostedTime(),
                0,
                0,
                postAddDTO.getPostPublicStatus(),
                postAddDTO.getTaggedUserEmailList()
        );
        return postRepository.save(post).getCaption()+" posted.";
    }
    @Override
    public String likePost(PostLikeDTO postLikeDTO) {
        try {
            if(likeRepository.findLikeByPostIdEquals(postLikeDTO.getPostID()).isPresent()){
                likeUnlikeCommon(postLikeDTO,"$addToSet");
            }else {
                likeRepository.save(new Like(postLikeDTO.getPostID(),List.of(postLikeDTO.getLikedUserEmail())));
                likedLogRepository.save(new LikedLog(postLikeDTO.getPostAuthorEmail(),postLikeDTO.getLikedUserEmail(),List.of(postLikeDTO.getPostID())));
            }
            Optional<Post> byId = postRepository.findById(postLikeDTO.getPostID());
            byId.get().setLikeCount(byId.get().getLikeCount()+1);
            postRepository.save(byId.get());
            return "success";
        }catch (Exception e){
            log.error(e.getMessage());
            return "Unsuccessful";
        }
    }
    @Override
    public List<PostViewDTO> getAllPostsByUserEmail(String userProfileEmail, String viewedUserEmail, int page) {
        Pageable pageable = PageRequest.of(page,10, Sort.by("postedTime").descending());
        Page<Post> postPage = postRepository.findPostsByPostAuthorEmailEquals(userProfileEmail,pageable);
        LikedLog likedLog = likedLogRepository.findLikedLogByAuthorEmailEqualsAndLikedUserEmailEquals(userProfileEmail,viewedUserEmail);
        List<PostViewDTO> postViewDTOList = postMappers.pageToPostViewList(postPage);
        if (likedLog != null) {
            Set<String> likedPostIds = new HashSet<>(likedLog.getLikedPostList());
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            postViewDTOList.parallelStream().forEach(postViewDTO -> {
                executorService.submit(() -> {
                    if (likedPostIds.contains(postViewDTO.getPostID())) {
                        postViewDTO.setViewedUserLikedStatus(true);
                    }
                });
            });
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return postViewDTOList;
    }
    @Override
    public Boolean sharePost(SharePostDTO sharePostDTO) {
        if (postRepository.findById(sharePostDTO.getSharedPostID()).isPresent()){
            SharedPost sharedPost = new SharedPost(
                    sharePostDTO.getSharedPostID(),
                    sharePostDTO.getSharedUserEmail(),
                    sharePostDTO.getSharedComment(),
                    sharePostDTO.getSharedTime()
                    );
            sharedPostRepository.save(sharedPost);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public List<AllPostViewDTO> getAllPosts(String userEmail, int postPageIndex) {
        try {
            MongoCollection<Document> followingCollection = userDataBase.getCollection("following");
            Document query = new Document("userEmail", userEmail);
            Document result = followingCollection.find(query).first();
            List<String> followingUserEmailList = result.getList("followingUserEmailList", String.class);
            Pageable pageablePost = PageRequest.of(postPageIndex, 50, Sort.by("postedTime").descending());
            Page<Post> postPage = postRepository.findPostsByPostAuthorEmailIn(followingUserEmailList, pageablePost);
            List<AllPostViewDTO> allPostViewDTOList = postMappers.PageToDtoList(postPage);
            MongoCollection<Document> likeCollection = mongoDatabase.getCollection("like");
            MongoCollection<Document> userCollection = vortexDataBase.getCollection("user");

            List<Future<?>> futures = new ArrayList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            for (AllPostViewDTO post : allPostViewDTOList) {
                futures.add(executorService.submit(() -> {
                    Document queryForFetchUser = new Document("email", post.getPostAuthorEmail());
                    Document userDocument = userCollection.find(queryForFetchUser).first();
                    String postId = post.getPostID();
                    Document likeQuery = new Document("postId", postId).append("likedEmailList", userEmail);
                    Document likeResult = likeCollection.find(likeQuery).first();
                    boolean userLikedStatus = likeResult != null;
                    post.setUserLikedStatus(userLikedStatus);
                    post.setPostAuthorName(
                            userDocument.getString("firstName") + " " + userDocument.getString("lastName"));
                    post.setPostAuthorProfileImageUrl(userDocument.getString("profilePhotoURL"));
                }));
            }
            for (Future<?> future : futures) {
                future.get();
            }
            executorService.shutdown();
            Pageable pageableSharedPost = PageRequest.of(postPageIndex, 100, Sort.by("sharedTime").descending());
            Page<SharedPost> sharedPostPage = sharedPostRepository.findAll(pageableSharedPost);
            for (SharedPost post : sharedPostPage) {
                Post post1 = postRepository.findById(post.getPostID()).get();
                Document queryForFetchAuthorUser = new Document("email", post1.getPostAuthorEmail());
                Document queryForFetchSharedUser = new Document("email", post.getSharedUserEmail());
                Document authorUserDocument = userCollection.find(queryForFetchAuthorUser).first();
                Document sharedUserDocument = userCollection.find(queryForFetchSharedUser).first();
                AllPostViewDTO allPostViewDTO = postMappers.postToAllPostViewDTO(post1);
                allPostViewDTO.setPostAuthorName(
                        authorUserDocument.getString("firstName") + " " + authorUserDocument.getString("lastName"));
                allPostViewDTO.setPostAuthorProfileImageUrl(authorUserDocument.getString("profilePhotoURL"));
                allPostViewDTO.setSharedUserEmail(post.getSharedUserEmail());
                allPostViewDTO.setSharedUserName(
                        sharedUserDocument.getString("firstName") + " " + sharedUserDocument.getString("lastName"));
                allPostViewDTO.setSharedUserProfileImageUrl(sharedUserDocument.getString("profilePhotoURL"));
                allPostViewDTO.setSharedComment(post.getSharedComment());
                allPostViewDTO.setSharedTime(post.getSharedTime());
                Document likeQuery = new Document("postId", post1.getPostID()).append("likedEmailList", userEmail);
                Document likeResult = likeCollection.find(likeQuery).first();
                boolean userLikedStatus = likeResult != null;
                allPostViewDTO.setUserLikedStatus(userLikedStatus);
                allPostViewDTOList.add(allPostViewDTO);
            }
            return allPostViewDTOList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public Boolean unlikePost(PostLikeDTO postLikeDTO) {
        if(likeRepository.findLikeByPostIdEquals(postLikeDTO.getPostID()).isPresent()){
            return likeUnlikeCommon(postLikeDTO,"$pull");
        }else {
            throw new PostNotFoundException();
        }
    }

    @Override
    public Boolean deletePost(String postID, String authorEmail) {
        if(postRepository.findById(postID).isPresent()){
            postRepository.deleteById(postID);
            likeRepository.findLikeByPostIdEquals(postID);

        }else{
            throw new PostNotFoundException();
        }
    }

    @Override
    public Boolean addComment(AddCommentDTO addCommentDTO) {
        if(postRepository.findById(addCommentDTO.getPostID()).isPresent()){
            if(commentRepository.findByPostIdEquals(addCommentDTO.getPostID()).isPresent()){
                return addDeleteCommentCommon(addCommentDTO,"$addToSet");
            }else {
                List<Comment> commentList = new ArrayList<>();
                Comment comment = new Comment(addCommentDTO.getCommentUserEmail(),addCommentDTO.getComment());
                commentList.add(comment);
                Comments comments = new Comments(addCommentDTO.getPostID(), commentList);
                commentRepository.save(comments);
                return true;
            }
        }else {
            throw new PostNotFoundException();
        }
    }
    @Override
    public Boolean deleteComment(AddCommentDTO addCommentDTO){
        if(commentRepository.findByPostIdEquals(addCommentDTO.getPostID()).isPresent()){
            return addDeleteCommentCommon(addCommentDTO,"$pull");
        }else{
            throw new CommentNotFoundException();
        }
    }
    private Boolean addDeleteCommentCommon(AddCommentDTO addCommentDTO,String addDeleteStatus){
        try {
            MongoCollection<Document> commentsCollection = mongoDatabase.getCollection("comments");
            UpdateOptions updateOptions = new UpdateOptions().upsert(true);
            Comment comment = new Comment(addCommentDTO.getCommentUserEmail(),addCommentDTO.getComment());
            UpdateResult updateResultComment = commentsCollection.updateOne(
                    new Document("postId",addCommentDTO.getPostID()),
                    new Document(addDeleteStatus,new Document("commentList",comment)),
                    updateOptions
            );
            log.info(updateResultComment.toString());
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;

        }
    }
    @Override
    public List<ViewCommentDTO> getAllComments(String postID, int pageIndex) {
        Optional<Comments> byPostIdEquals = commentRepository.findByPostIdEquals(postID);
        if(byPostIdEquals.isPresent()){
            Comments comments = byPostIdEquals.get();
            List<Comment> commentList = comments.getCommentList();
            List<ViewCommentDTO> viewCommentDTOS = new ArrayList<>();
        }else  {
            throw new PostNotFoundException();
        }
    }

    @Override
    public List<UserByEmailDTO> getAllLikelist(String postID, int pageIndex) {
        Optional<Post> byId  = postRepository.findById(postID);
        if(byId.isPresent()){
            PageRequest of = PageRequest.of(pageIndex, 100);
            Optional<Like> likeByPostIdEquals = likeRepository.findLikeByPostIdEquals(postID);
            return authServiceProxy.getFollowingDataList(likeByPostIdEquals.get().getLikedEmailList());
        }else {
            throw new PostNotFoundException();
        }
    }

    private Boolean likeUnlikeCommon(PostLikeDTO postLikeDTO,String likeOrUnlikeStatus){
        try {
            MongoCollection<Document> likeCollection = mongoDatabase.getCollection("like");
            MongoCollection<Document> likedLogCollection = mongoDatabase.getCollection("likedlog");
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult updateResultLike = likeCollection.updateOne(
                    new Document("postId",postLikeDTO.getPostID()),
                    new Document(likeOrUnlikeStatus, new Document("likedEmailList",postLikeDTO.getLikedUserEmail())),
                    options
            );
            UpdateResult updateResultForLikedLog = likedLogCollection.updateOne(
                    new Document("$and", Arrays.asList(
                            new Document("likedUserEmail", postLikeDTO.getLikedUserEmail()),
                            new Document("authorEmail", postLikeDTO.getPostAuthorEmail())
                    )),
                    new Document(likeOrUnlikeStatus, new Document("likedPostList",postLikeDTO.getPostID())),
                    options
            );
            Optional<Post> byId = postRepository.findById(postLikeDTO.getPostID());
            if(likeOrUnlikeStatus.equals("$pull")){
                byId.get().setLikeCount(byId.get().getLikeCount()-1);
            }
            postRepository.save(byId.get());
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
}
