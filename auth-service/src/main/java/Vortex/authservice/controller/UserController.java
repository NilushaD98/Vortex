package Vortex.authservice.controller;

import Vortex.authservice.dto.UserDTO;
import Vortex.authservice.dto.request.*;
import Vortex.authservice.dto.response.AuthResponseDTO;
import Vortex.authservice.dto.response.OtpResponse;
import Vortex.authservice.dto.response.UserByEmailDTO;
import Vortex.authservice.service.UserService;
import Vortex.authservice.util.StandardResponse;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vortexcoreservice/api/v1/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("user_sign_up")
    public ResponseEntity<StandardResponse> userSignUp(@RequestBody UserSignUpDTO userSignUpDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(201,"User Sign Up Status : ",userService.userSignUp(userSignUpDTO)), HttpStatus.CREATED
        );
    }
    @PostMapping("seller_sign_up")
    public ResponseEntity<StandardResponse> sellerSignUp(@RequestBody SellerSignUpDTO sellerSignUpDTO){
        AuthResponseDTO authResponseDTO = userService.sellerSignUp(sellerSignUpDTO);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(201,"Seller Sign Up Status : ",authResponseDTO),HttpStatus.OK
        );
    }
    @GetMapping("find_account_by_email")
    public ResponseEntity<StandardResponse> findAccountById(@RequestParam("email")String email){
        UserByEmailDTO user = userService.getUserbyEmail(email);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"User By Email : ",user),HttpStatus.OK
        );
    }
    @GetMapping("get_user_by_id")
    public ResponseEntity<StandardResponse> getUserById(@RequestParam("user_id") String user_id){
        UserByEmailDTO userByEmailDTO = userService.getUserById(user_id);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(302,"App User ", userByEmailDTO),HttpStatus.FOUND
        );
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("update_user_public_details")
    public ResponseEntity<StandardResponse> userBioAndProfilePictureUpdate(@RequestBody UpdateUserPublicDetailsDTO updateUserPublicDetailsDTO){
        String updateStatus = userService.updateUserDetails(updateUserPublicDetailsDTO);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"User Public Details Update Status : ", updateStatus),HttpStatus.OK
        );
    }
    @GetMapping("send_otp_for_two_step_verification")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> sendOTPtoEmail(@RequestParam("email")String email){
        OtpResponse otpResponse = userService.sendOtpToEmail(email);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Otp Mail Send Status : ",otpResponse),HttpStatus.OK
        );
    }
    @PostMapping("check_otp_for_two_step_verification")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> checkOTP(@RequestBody CheckOTPDTO checkOTPDTO){
        boolean otpCheckStatus = userService.checkOTP(checkOTPDTO);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"OTP Checked Status : ", otpCheckStatus),HttpStatus.OK
        );
    }
    //for user service and post service
    @PostMapping("get_following_data_list")
    public List<FollowerDetailsDTO> getFollowingDataList(@RequestBody List<String> followingUserEmailList){
        return userService.getFollowingDataList(followingUserEmailList);
    }
    //for user service and post service
    @PostMapping("get_followers_data_list")
    public List<FollowerDetailsDTO> getFollowersDataList(@RequestBody List<String> followersUserEmailList){
        return userService.getFollowersDataList(followersUserEmailList);
    }
    @GetMapping("user_by_email")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> userByEmail(@RequestParam("email")String email){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"User by Email : ",userService.userByEmail(email)),HttpStatus.OK
        );
    }
    @PutMapping("update_user_profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> updateUser(@RequestBody UserDTO userDTO){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"User Update Status ",userService.userUpdate(userDTO)),HttpStatus.OK
        );
    }
    @GetMapping("view_another_user_profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StandardResponse> viewAnotherUser(
            @RequestParam("viewUserEmail") String viewUserEmail,
            @RequestParam("userEmail") String userEmail
    ){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"View Another User",userService.viewAnotherUser(userEmail,viewUserEmail)),HttpStatus.OK
        );
    }
}
