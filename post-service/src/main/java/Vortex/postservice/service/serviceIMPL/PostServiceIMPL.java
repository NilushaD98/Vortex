package Vortex.postservice.service.serviceIMPL;

import Vortex.postservice.collection.*;
import Vortex.postservice.dto.CommentsDTO;
import Vortex.postservice.dto.request.*;
import Vortex.postservice.dto.response.*;
import Vortex.postservice.exception.CommentNotFoundException;
import Vortex.postservice.exception.PostNotFoundException;
import Vortex.postservice.feign.AuthServiceProxy;
import Vortex.postservice.repositories.*;
import Vortex.postservice.service.PostService;
import Vortex.postservice.util.mappers.PostMappers;
import Vortex.postservice.util.mappers.ReportedPostMapper;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.ClassUtils.isPresent;

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
    @Autowired
    private ReportedPostRepository reportedPostRepository;
    @Autowired
    private ReportedPostMapper reportedPostMapper;
    @Autowired
    private ReplyCommentRepository replyCommentRepository;
    @Autowired
    private AuthorizedUserService authorizedUserService;
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

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
                if(likedLogRepository.findLikedLogByAuthorEmailEqualsAndLikedUserEmailEquals(postLikeDTO.getPostAuthorEmail(),postLikeDTO.getLikedUserEmail()) == null){
                    likedLogRepository.save(new LikedLog(postLikeDTO.getPostAuthorEmail(),postLikeDTO.getLikedUserEmail(),List.of(postLikeDTO.getPostID())));
                }else {
                    MongoCollection<Document> likedLogCollection = mongoDatabase.getCollection("likedlog");
                    UpdateOptions options = new UpdateOptions().upsert(true);
                    UpdateResult updateResultForLikedLog = likedLogCollection.updateOne(
                            new Document("$and", Arrays.asList(
                                    new Document("likedUserEmail", postLikeDTO.getLikedUserEmail()),
                                    new Document("authorEmail", postLikeDTO.getPostAuthorEmail())
                            )),
                            new Document("$addToSet", new Document("likedPostList",postLikeDTO.getPostID())),
                            options
                    );
                }
            }
            Optional<Post> byId = postRepository.findById(postLikeDTO.getPostID());
            byId.get().setLikeCount(byId.get().getLikeCount()+1);
            postRepository.save(byId.get());
            if(!postLikeDTO.getLikedUserEmail().equals(postLikeDTO.getPostAuthorEmail())){
                kafkaTemplate.send("like",postLikeDTO);
            }
            return "success";
        }catch (Exception e){
            log.error(e.getMessage());
            return "Unsuccessful";
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
    @Override
    public List<PostViewDTO> getAllPostsByUserEmail(String userProfileEmail, String viewedUserEmail, int page) {
        Pageable pageable = PageRequest.of(page,10, Sort.by("postedTime").descending());
        Page<Post> postPage = postRepository.findPostsByPostAuthorEmailEquals(userProfileEmail,pageable);
        LikedLog likedLog = likedLogRepository.findLikedLogByAuthorEmailEqualsAndLikedUserEmailEquals(userProfileEmail,viewedUserEmail);
        List<PostViewDTO> postViewDTOList = postMappers.pageToPostViewList(postPage);
        List<String> userList = new ArrayList<>();
        userList.add(userProfileEmail);
        List<UserByEmailDTO> user = authServiceProxy.getFollowingDataList(userList);
        UserByEmailDTO user1 = user.get(0);
        for(PostViewDTO postViewDTO : postViewDTOList){
            postViewDTO.setPostAuthor(user1.getFirstName()+" "+user1.getLastName());
        }
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
                    post.setUserFollowedStatus(true);
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
            if(!sharedPostPage.isEmpty()){
                for (SharedPost post : sharedPostPage) {
                    Post post1 = postRepository.findById(post.getPostID()).get();
                    Document queryForFetchAuthorUser = new Document("email", post1.getPostAuthorEmail());
                    Document queryForFetchSharedUser = new Document("email", post.getSharedUserEmail());
                    Document authorUserDocument = userCollection.find(queryForFetchAuthorUser).first();
                    Document sharedUserDocument = userCollection.find(queryForFetchSharedUser).first();
                    AllPostViewDTO allPostViewDTO = postMappers.postToAllPostViewDTO(post1);
                    allPostViewDTO.setUserFollowedStatus(true);
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
            }

            return allPostViewDTOList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalError("server error");
        }
    }

    @Override
    public List<AllPostViewDTO> getAllHomePagePosts(String userEmail, int postPageIndex) {

        try {
            Pageable pageablePost = PageRequest.of(postPageIndex, 50, Sort.by("postedTime").descending());
            Page<Post> postPage = postRepository.findAll(pageablePost);
            List<AllPostViewDTO> allPostViewDTOList = postMappers.PageToDtoList(postPage);
            MongoCollection<Document> likeCollection = mongoDatabase.getCollection("like");
            MongoCollection<Document> userCollection = vortexDataBase.getCollection("user");
            MongoCollection<Document> followingCollection = userDataBase.getCollection("following");

            List<Future<?>> futures = new ArrayList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            for (AllPostViewDTO post : allPostViewDTOList) {
                futures.add(executorService.submit(() -> {
                    Document queryForFetchUser = new Document("email", post.getPostAuthorEmail());
                    Document queryForGetFoll0wingList = new Document("userEmail",userEmail).append("followingUserEmailList",post.getPostAuthorEmail());
                    Document followResult = followingCollection.find(queryForGetFoll0wingList).first();
                    Document userDocument = userCollection.find(queryForFetchUser).first();
                    String postId = post.getPostID();
                    Document likeQuery = new Document("postId", postId).append("likedEmailList", userEmail);
                    Document likeResult = likeCollection.find(likeQuery).first();
                    boolean userLikedStatus = likeResult != null;
                    boolean userFollowStatus = followResult != null;
                    post.setUserFollowedStatus(userFollowStatus);
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
            return allPostViewDTOList;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalError("server error");
        }
    }

    @Override
    public Boolean removeUserPosts(String userEmail) {
        postRepository.deleteAllByPostAuthorEmailEquals(userEmail);
        commentRepository.deleteAllByCommentUserEmailEquals(userEmail);
        likedLogRepository.deleteAllByAuthorEmailEquals(userEmail);
        likedLogRepository.deleteAllByLikedUserEmail(userEmail);
        replyCommentRepository.deleteAllByRepliedUserEmailEquals(userEmail);
        return true;
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
        Optional<Post> byId = postRepository.findById(postID);
        if(byId.isPresent() && byId.get().getPostAuthorEmail().equals(authorEmail)){
            postRepository.deleteById(postID);
            likeRepository.deleteByPostIdEquals(postID);
            commentRepository.deleteByPostIdEquals(postID);
            return true;
        }else{
            throw new PostNotFoundException();
        }
    }

    @Override
    public Boolean addComment(AddCommentDTO addCommentDTO) {
        Optional<Post> byId = postRepository.findById(addCommentDTO.getPostID());
        if(byId.isPresent()){
            Comments comments = new Comments(
                    addCommentDTO.getPostID(),
                    addCommentDTO.getComment(),
                    addCommentDTO.getCommentUserEmail()

            );
            commentRepository.save(comments);
            byId.get().setCommentCount(byId.get().getCommentCount()+1);
            postRepository.save(byId.get());
            if(!byId.get().getPostAuthorEmail().equals(addCommentDTO.getCommentUserEmail())){
                kafkaTemplate.send("comment",addCommentDTO);
            }
            return true;
        }else {
            throw new PostNotFoundException();
        }
    }
    @Override
    public Boolean replyComment(ReplyCommentDTO replyCommentDTO) {
        Optional<Comments> byId = commentRepository.findById(replyCommentDTO.getMainCommentID());
        if(byId.isPresent()){
            ReplyComment replyComment = new ReplyComment(
                    UUID.randomUUID().toString().substring(1,10),
                    replyCommentDTO.getRepliedComment(),
                    replyCommentDTO.getRepliedUserEmail(),
                    replyCommentDTO.getMainCommentID()
            );
            replyCommentRepository.save(replyComment);
            commentRepository.save(byId.get());
            if(byId.get().getReplyCommentsIDList() == null){
                byId.get().setReplyCommentsIDList(List.of(replyComment.getRepliedCommentID()));
            }else {
                byId.get().getReplyCommentsIDList().add(replyComment.getRepliedCommentID());
            }
            commentRepository.save(byId.get());
            return true;
        }else {
            throw new CommentNotFoundException();
        }
    }

    @Override
    public Boolean likeComment(LikeCommentDTO likeCommentDTO) {
        Comments comments = commentRepository.findById(likeCommentDTO.getLikedCommentId()).get();
        comments.setCommentLikeCount(comments.getCommentLikeCount()+1);
        if(comments.getLikedUserList() == null){
            comments.setLikedUserList(List.of(likeCommentDTO.getLikedUserEmail()));
        }else {
            comments.getLikedUserList().add(likeCommentDTO.getLikedUserEmail());
        }
        commentRepository.save(comments);
        return true;
    }

    @Override
    public Boolean likeReplyComment(LikeReplyCommentDTO likeReplyCommentDTO) {
        Optional<Comments> byId = commentRepository.findById(likeReplyCommentDTO.getMainCommentID());
        Optional<ReplyComment> replyCommentById = replyCommentRepository.findById(likeReplyCommentDTO.getRepliedCommentID());
        if (byId.isPresent() && replyCommentById.isPresent()){
            if(replyCommentById.get().getLikedUserEmailList() ==null){
                replyCommentById.get().setLikedCount(1);
                replyCommentById.get().setLikedUserEmailList(List.of(likeReplyCommentDTO.getLikedUserEmail()));
            }else {
                replyCommentById.get().setLikedCount(replyCommentById.get().getLikedCount()+1);
                replyCommentById.get().getLikedUserEmailList().add(likeReplyCommentDTO.getLikedUserEmail());
            }
            replyCommentRepository.save(replyCommentById.get());
            return true;
        }else {
            throw new CommentNotFoundException();
        }
    }

    @Override
    public List<ViewReplyCommentDTO> getAllReplyComments(String commentID) {
        Optional<Comments> commentsByCommentIDEquals = commentRepository.findCommentsByCommentIDEquals(commentID);
        if(commentsByCommentIDEquals.isPresent()){
            List<ViewReplyCommentDTO> viewReplyCommentDTOList = new ArrayList<>();
            List<ReplyComment> replyCommentList  = replyCommentRepository.findByRepliedCommentIDIn(commentsByCommentIDEquals.get().getReplyCommentsIDList());
            String authorizedUser = authorizedUserService.getAuthorizedUser();
            for(ReplyComment replyComment:replyCommentList){
                UserByEmailDTO data = authServiceProxy.getFollowingDataList(List.of(replyComment.getRepliedUserEmail())).get(0);
                ViewReplyCommentDTO viewReplyCommentDTO = new ViewReplyCommentDTO();
                viewReplyCommentDTO.setRepliedCommentID(replyComment.getRepliedCommentID());
                viewReplyCommentDTO.setRepliedComment(replyComment.getRepliedComment());
                viewReplyCommentDTO.setRepliedUserEmail(replyComment.getRepliedUserEmail());
                viewReplyCommentDTO.setRepliedUserName((data.getFirstName()+" "+data.getLastName()));
                viewReplyCommentDTO.setRepliedUserProfilePictureURL(data.getProfilePhotoURL());
                viewReplyCommentDTO.setMainCommentID(replyComment.getMainCommentID());
                viewReplyCommentDTO.setUserLikedStatus(false);
                if(replyComment.getLikedCount() != 0){
                    viewReplyCommentDTO.setLikedCount(replyComment.getLikedCount());
                    viewReplyCommentDTO.setLikedUsersList(authServiceProxy.getFollowingDataList(replyComment.getLikedUserEmailList()));
                    if(replyComment.getLikedUserEmailList().contains(authorizedUser)) {
                        viewReplyCommentDTO.setUserLikedStatus(true);
                    }
                }else {
                    viewReplyCommentDTO.setLikedCount(0);
                    viewReplyCommentDTO.setLikedUsersList(new ArrayList<>());
                }
                viewReplyCommentDTO.setLikedCount(replyComment.getLikedCount());
                viewReplyCommentDTOList.add(viewReplyCommentDTO);
            }
            return viewReplyCommentDTOList;
        }else {
            throw new CommentNotFoundException();
        }
    }

    @Override
    public Boolean deleteReplyComment(String replyCommentId) {
        Optional<ReplyComment> byId = replyCommentRepository.findById(replyCommentId);
        if(byId.isPresent()){
            replyCommentRepository.deleteById(replyCommentId);
            return true;
        }else {
            throw new CommentNotFoundException();
        }
    }


    @Override
    public Boolean deleteComment(DeleteCommentDTO deleteCommentDTO){
        Optional<Comments> commentsById=commentRepository.findCommentsByCommentIDEquals(deleteCommentDTO.getCommentID());
        if(commentsById.isPresent() && deleteCommentDTO.getAuthorEmail().equals(commentsById.get().getCommentUserEmail())){
            if(commentsById.get().getReplyCommentsIDList() != null){
                replyCommentRepository.deleteAllById(commentsById.get().getReplyCommentsIDList());
            }
            commentRepository.deleteById(deleteCommentDTO.getCommentID());
            return true;
        }else {
            throw new CommentNotFoundException();
        }
    }
    @Override
    public CommentsDTO getAllComments(String postID ) {
        CommentsDTO commentsDTO = new CommentsDTO();
        List<ViewCommentDTO> viewCommentDTOList = new ArrayList<>();
        List<List<ViewReplyCommentDTO>> viewReplyCommentDTOList = new ArrayList<>();
        List<Comments> byPostIdEquals = commentRepository.findByPostIdEquals(postID);
        String authorizedUser = authorizedUserService.getAuthorizedUser();
        for(Comments comments : byPostIdEquals){
                int count = replyCommentRepository.countByMainCommentIDEquals(comments.getCommentID());
                UserByEmailDTO userByEmailDTO = authServiceProxy.getUser(comments.getCommentUserEmail());
                ViewCommentDTO viewCommentDTO = new ViewCommentDTO();
                viewCommentDTO.setCommentID(comments.getCommentID());
                viewCommentDTO.setCommentedUserEmail(comments.getCommentUserEmail());
                viewCommentDTO.setCommentedUserName((userByEmailDTO.getFirstName()+" "+userByEmailDTO.getLastName()));
                viewCommentDTO.setCommentedUserProfilePictureURL(userByEmailDTO.getProfilePhotoURL());
                viewCommentDTO.setComment(comments.getComment());
                viewCommentDTO.setCommentLikeCount(comments.getCommentLikeCount());
                viewCommentDTO.setUserLikedStatus(false);
            if(count>0){
                    viewCommentDTO.setReplyCommentCount(count);
                List<ViewReplyCommentDTO> allReplyComments = getAllReplyComments(comments.getCommentID());
                viewReplyCommentDTOList.add(allReplyComments);
            }else {
                    viewCommentDTO.setReplyCommentCount(0);
                    viewReplyCommentDTOList.add(new ArrayList<>());
                }
            if(comments.getLikedUserList() != null){
                viewCommentDTO.setLikedUsersList(authServiceProxy.getFollowingDataList(comments.getLikedUserList()));
                log.info(viewCommentDTO.getLikedUsersList().toString());
                if(comments.getLikedUserList().contains(authorizedUser)){
                    viewCommentDTO.setUserLikedStatus(true);
                }
            }else{
                viewCommentDTO.setLikedUsersList(new ArrayList<>());
            }
                viewCommentDTOList.add(viewCommentDTO);
            }
            commentsDTO.setMainComments(viewCommentDTOList);
            commentsDTO.setRepliesComment(viewReplyCommentDTOList);
            return commentsDTO;
    }
    @Override
    public List<UserByEmailDTO> getAllLikelist(String postID) {
        Optional<Post> byId  = postRepository.findById(postID);
        if(byId.isPresent()){
            Optional<Like> likeByPostIdEquals = likeRepository.findLikeByPostIdEquals(postID);
            return authServiceProxy.getFollowingDataList(likeByPostIdEquals.get().getLikedEmailList());
        }else {
            throw new PostNotFoundException();
        }
    }
    @Override
    public String reportPost(PostReportDTO postReportDTO) {
        reportedPostRepository.save(reportedPostMapper.DTOToEntity(postReportDTO));
        return "success";
    }
    @Override
    public List<ReportedPostDTO> getAllReportedPosts() {
        List<ReportedPostDTO> reportedPostDTOList = new ArrayList<>();
        List<ReportedPost> allReportedPosts = reportedPostRepository.findAll();
        for (ReportedPost reportedPost:allReportedPosts){
            Optional<Post> byId = postRepository.findById(reportedPost.getPostID());
            ReportedPostDTO reportedPostDTO = new ReportedPostDTO();
            reportedPostDTO.setPostID(byId.get().getPostID());
            reportedPostDTO.setPostAuthorEmail(byId.get().getPostAuthorEmail());
            reportedPostDTO.setMediaLink(byId.get().getMediaLink());
            reportedPostDTO.setCaption(byId.get().getCaption());
            reportedPostDTO.setPostedTime(byId.get().getPostedTime());
            reportedPostDTO.setLikeCount(byId.get().getLikeCount());
            reportedPostDTO.setCommentCount(byId.get().getCommentCount());
            reportedPostDTO.setReportedUserEmail(reportedPostDTO.getReportedUserEmail());
            reportedPostDTO.setReportedReason(reportedPostDTO.getReportedReason());
            UserByEmailDTO postAuthor = (UserByEmailDTO)authServiceProxy.findAccountById(byId.get().getPostAuthorEmail()).getBody().getData();
            UserByEmailDTO reportedUser = (UserByEmailDTO)authServiceProxy.findAccountById(reportedPost.getReportedUserEmail()).getBody().getData();
            reportedPostDTO.setPostAuthorName(postAuthor.getFirstName()+" "+postAuthor.getLastName());
            reportedPostDTO.setPostAuthorProfilePhotoURL(postAuthor.getProfilePhotoURL());
            reportedPostDTO.setReportedUserName(reportedUser.getFirstName()+" "+reportedUser.getLastName());
            reportedPostDTO.setReportedUserProfilePhotoURL(reportedUser.getProfilePhotoURL());
            reportedPostDTOList.add(reportedPostDTO);
        }
        return reportedPostDTOList;
    }

}
