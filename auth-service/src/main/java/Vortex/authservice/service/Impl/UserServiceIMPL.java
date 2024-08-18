package Vortex.authservice.service.Impl;

import Vortex.authservice.dto.UserDTO;
import Vortex.authservice.dto.request.*;
import Vortex.authservice.dto.response.AuthResponseDTO;
import Vortex.authservice.dto.response.OtpResponse;
import Vortex.authservice.dto.response.UserByEmailDTO;
import Vortex.authservice.entity.OTP;
import Vortex.authservice.entity.SellerDetails;
import Vortex.authservice.entity.User;
import Vortex.authservice.enums.Roles;
import Vortex.authservice.exceptions.EmailSenderErrorResponse;
import Vortex.authservice.exceptions.UserAlreadyReportedException;
import Vortex.authservice.exceptions.UserNotFoundException;
import Vortex.authservice.feign.PostServiceProxy;
import Vortex.authservice.feign.UserServiceProxy;
import Vortex.authservice.repository.OTPRepository;
import Vortex.authservice.repository.SellerDetailsRepository;
import Vortex.authservice.repository.UserRepository;
import Vortex.authservice.service.AuthService;
import Vortex.authservice.service.UserService;
import Vortex.authservice.util.mappers.UserMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static Vortex.authservice.enums.Roles.USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceIMPL implements UserService{

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final JavaMailSender mailSender;
    private final OTPRepository otpRepository;
    private final SellerDetailsRepository sellerDetailsRepository;
    private final UserMapper userMapper;
    private final UserServiceProxy userServiceProxy;
    private final PostServiceProxy postServiceProxy;



    @Override
    public AuthResponseDTO userSignUp(UserSignUpDTO userSignUpDTO) {
        if(userRepository.findByEmailEquals(userSignUpDTO.getEmail()).isPresent()){
            throw new UserAlreadyReportedException();
        }else {
            User user = new User(
                    userSignUpDTO.getFirstName(),
                    userSignUpDTO.getLastName(),
                    userSignUpDTO.getBirthDay(),
                    userSignUpDTO.getEmail(),
                    userSignUpDTO.getContact(),
                    userSignUpDTO.getCountry(),
                    passwordEncoder.encode(userSignUpDTO.getPassword()),
                    USER
            );
            userRepository.save(user);
            userServiceProxy.initializeFollowingAndFollowersLis(userSignUpDTO.getEmail());
            AuthResponseDTO authenticateResponse = authService.authenticate(new DefaultAuthenticationDTO(
                    userSignUpDTO.getEmail(),
                    userSignUpDTO.getPassword()
            ));
            return authenticateResponse;
        }
    }
    @Override
    public AuthResponseDTO googleSignUp(GoogleSignUpDTO googleSignUpDTO) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String customPassword = uuid.substring(0, Math.min(8, uuid.length()));
        Optional<User> byEmailEquals = userRepository.findByEmailEquals(googleSignUpDTO.getEmail());
        Date date = new Date(0);
        if (byEmailEquals.isEmpty()){
            User user = new User();
            user.setFirstName(googleSignUpDTO.getFirstName());
            user.setLastName(googleSignUpDTO.getLastName());
            user.setBirthDay(date);
            user.setEmail(googleSignUpDTO.getEmail());
            user.setContact("0700000000");
            user.setCountry("country");
            user.setPassword(passwordEncoder.encode(customPassword));
            user.setProfilePhotoURL(googleSignUpDTO.getProfilePhotoURL());
            user.setRole(USER);
            userRepository.save(user);
            userServiceProxy.initializeFollowingAndFollowersLis(googleSignUpDTO.getEmail());
        }else {
            byEmailEquals.get().setPassword(passwordEncoder.encode(customPassword));
            userRepository.save(byEmailEquals.get());
        }
        authResponseDTO = authService.authenticate(new DefaultAuthenticationDTO(googleSignUpDTO.getEmail(),customPassword));
        return authResponseDTO;
    }

    @Override
    public List<FollowerDetailsDTO> searchUser(String username) {
        List<FollowerDetailsDTO> searchResult = new ArrayList<>();

        List<User> userList = userRepository.findUserByFirstNamePattern(username);
        if(userList != null){
            List<FollowerDetailsDTO> followerDetailsDTOList = userMapper.EntityTOFollowerDetailsDTO(userList);
            searchResult.addAll(followerDetailsDTOList);
        }
        List<User> usersList = userRepository.findUserByLastNamePattern(username);
        if(usersList != null){
            List<FollowerDetailsDTO> followerDetailsDTOList = userMapper.EntityTOFollowerDetailsDTO(usersList);
            searchResult.addAll(followerDetailsDTOList);
        }
        return searchResult;
    }

    @Override
    public Boolean removeUser(String userEmail) {
        postServiceProxy.removeUser(userEmail);
        userServiceProxy.removeUser(userEmail);
        Optional<User> byEmailEquals = userRepository.findByEmailEquals(userEmail);
        if(byEmailEquals.isPresent()){
            userRepository.deleteById(byEmailEquals.get().getUserid());
        }
        return true;
    }

    @Override
    public UserByEmailDTO getUserById(String userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()){
            User user = byId.get();
            UserByEmailDTO userEmailDTO = new UserByEmailDTO(
                    user.getProfilePhotoURL(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getCountry()
            );
            return userEmailDTO;
        }else {
            throw new UserNotFoundException();
        }
    }
    @Override
    public String updateUserDetails(UpdateUserPublicDetailsDTO updateUserPublicDetailsDTO) {
        Optional<User> byEmailEquals = userRepository.findByEmailEquals(updateUserPublicDetailsDTO.getEmail());
        if(byEmailEquals.isPresent()){
            User user = byEmailEquals.get();
            user.setBio(updateUserPublicDetailsDTO.getBio());
            user.setProfilePhotoURL(updateUserPublicDetailsDTO.getProfilePhotoURL());
            return userRepository.save(user).getFirstName()+" updated";
        }else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public UserByEmailDTO getUserbyEmail(String email) {
        Optional<User> byEmailEquals = userRepository.findByEmailEquals(email);
        if(byEmailEquals.isPresent()){
            User user = byEmailEquals.get();
            return new UserByEmailDTO(
                    user.getProfilePhotoURL(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getCountry()
            );
        }else {
            throw new UserNotFoundException();
        }
    }
    @Override
    public OtpResponse sendOtpToEmail(String email) {
        int otp = generateRandomOTP();
        boolean vortexOtp = mailSender(email, "Vortex OTP", "V - " + Integer.toString(otp)+". If you didn't request this, simply ignore this message.\n" +
                "\n" +
                "Yours,\n" +
                "The Vortex Team");
        if(vortexOtp){
            OTP otp1 = new OTP(
                    email,
                    Integer.toString(otp),
                    new Date(System.currentTimeMillis()),
                    new Date(System.currentTimeMillis()+ 1*60*1000),//1 min otp
                    false
            );
            otpRepository.save(otp1);
            return new OtpResponse(true);
        }else {
            throw new EmailSenderErrorResponse();
        }
    }
    @Override
    public boolean checkOTP(CheckOTPDTO checkOTPDTO) {
        Optional<OTP> otp = otpRepository.findByOtpEquals(checkOTPDTO.getOtp());
        if (otp.isPresent()){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public AuthResponseDTO sellerSignUp(SellerSignUpDTO sellerSignUpDTO) {
        Optional<User> byEmailEquals = userRepository.findByEmailEquals(sellerSignUpDTO.getEmail());
        if(byEmailEquals.isPresent()){
            throw new UserAlreadyReportedException();
        }else {
            User user = new User(
                sellerSignUpDTO.getFirstName(),
                    sellerSignUpDTO.getLastName(),
                    null,
                    sellerSignUpDTO.getEmail(),
                    sellerSignUpDTO.getContact(),
                    sellerSignUpDTO.getCountry(),
                    passwordEncoder.encode(sellerSignUpDTO.getPassword()),
                    Roles.SELLER
            );
            userRepository.save(user);
            SellerDetails sellerDetails = new SellerDetails(
                    user.getUserid(),
                    sellerSignUpDTO.getNic(),
                    sellerSignUpDTO.getMetaMaskID(),
                    sellerSignUpDTO.getAddress()
            );
            sellerDetailsRepository.save(sellerDetails);
            return authService.authenticate(new DefaultAuthenticationDTO(sellerSignUpDTO.getEmail(),sellerSignUpDTO.getPassword()));
        }
    }
    @Override
    public List<FollowerDetailsDTO> getFollowingDataList(List<String> followingUserEmailList) {
        return userMapper.EntityTOFollowerDetailsDTO(userRepository.findByEmailIn(followingUserEmailList));
    }
    @Override
    public List<FollowerDetailsDTO> getFollowersDataList(List<String> followersUserEmailList) {
        return userMapper.EntityTOFollowerDetailsDTO(userRepository.findByEmailIn(followersUserEmailList));
    }
    @Override
    public UserDTO userByEmail(String email) {
        Optional<User> user = userRepository.findByEmailEquals(email);
        if(user.isPresent()){
            UserDTO userDTO = userMapper.EntityToDTO(user.get());
            if(userDTO.getBio() == null){
                userDTO.setBio("");
            }
            return userDTO;
        }else {
            throw new UserNotFoundException();
        }
    }
    @Override
    public Boolean userUpdate(UserDTO userDTO) {
        Optional<User> user = userRepository.findByEmailEquals(userDTO.getEmail());
        if(user.isPresent()){
            User updateUserEntity = user.get();
            updateUserEntity.setFirstName(userDTO.getFirstName());
            updateUserEntity.setLastName(userDTO.getLastName());
            updateUserEntity.setBirthDay(userDTO.getBirthDay());
            updateUserEntity.setContact(userDTO.getContact());
            updateUserEntity.setCountry(userDTO.getCountry());
            updateUserEntity.setBio(userDTO.getBio());
            updateUserEntity.setProfilePhotoURL(userDTO.getProfilePhotoURL());
            userRepository.save(updateUserEntity);
            return true;
        }else {
            throw new UserNotFoundException();
        }
    }
    @Override
    public UserDTO viewAnotherUser(String userEmail, String viewUserEmail) {
        MongoClient mongoClient = MongoClients.create("mongodb+srv://root:1234@cluster0.ucithrp.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("user");
        MongoCollection<Document> following = mongoDatabase.getCollection("following");
        Document checkIfFollow = new Document("userEmail", viewUserEmail).append("likedEmailList", userEmail);
        Document likeResult = following.find(checkIfFollow).first();
        boolean userLikedStatus = likeResult != null;
        Optional<User> user = userRepository.findByEmailEquals(userEmail);
        if(user.isPresent()){
            UserDTO userDTO = userMapper.EntityToDTO(user.get());
            if(userDTO.getBio() == null){
                userDTO.setBio("");
            }
            userDTO.setFollowedStatus(userLikedStatus);
            return userDTO;
        }else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Boolean updateDeliveryDetails(AddDeliverDetailsDTO addDeliverDetailsDTO) {
        Optional<User> byEmailEquals = userRepository.findByEmailEquals(addDeliverDetailsDTO.getEmail());
        if(byEmailEquals.isPresent()){
            byEmailEquals.get().setDeliveryAddress(addDeliverDetailsDTO.getAddress());
            byEmailEquals.get().setNic(addDeliverDetailsDTO.getNic());
            userRepository.save(byEmailEquals.get());
            return true;
        }else {
            throw new UserNotFoundException();
        }
    }



    public static int generateRandomOTP() {
        int min = 100000;
        int max = 999999;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
    public boolean mailSender(String toMail, String subject, String body){
        try {
            SimpleMailMessage newMail = new SimpleMailMessage();
            newMail.setTo(toMail);
            newMail.setSubject(subject);
            newMail.setText(body);
            mailSender.send(newMail);
            log.info("Mail successfully send to "+ toMail);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    @Scheduled(fixedRate = 1*10*1000)
    public void cleanupExpiredOTPRecords() {
        Date currentTime = new Date(System.currentTimeMillis());
        otpRepository.deleteOTPSByOtpExpirationTimeBefore(currentTime);
    }
}
