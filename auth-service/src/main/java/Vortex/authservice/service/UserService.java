package Vortex.authservice.service;

import Vortex.authservice.dto.UserDTO;
import Vortex.authservice.dto.request.*;
import Vortex.authservice.dto.response.AuthResponseDTO;
import Vortex.authservice.dto.response.OtpResponse;
import Vortex.authservice.dto.response.SellerDetailsDTO;
import Vortex.authservice.dto.response.UserByEmailDTO;

import java.util.List;

public interface UserService {
    AuthResponseDTO userSignUp(UserSignUpDTO userSignUpDTO);

    UserByEmailDTO getUserById(String userId);

    String updateUserDetails(UpdateUserPublicDetailsDTO updateUserPublicDetailsDTO);

    UserByEmailDTO getUserbyEmail(String email);

    OtpResponse sendOtpToEmail(String email);

    boolean checkOTP(CheckOTPDTO checkOTPDTO);

    Boolean sellerSignUp(SellerSignUpDTO sellerSignUpDTO);

    List<FollowerDetailsDTO> getFollowingDataList(List<String> followingUserEmailList);

    List<FollowerDetailsDTO> getFollowersDataList(List<String> followersUserEmailList);

    UserDTO userByEmail(String email);

    Boolean userUpdate(UserDTO userDTO);

    UserDTO viewAnotherUser(String userEmail, String viewUserEmail);

    Boolean updateDeliveryDetails(AddDeliverDetailsDTO addDeliverDetailsDTO);

    AuthResponseDTO googleSignUp(GoogleSignUpDTO googleSignUpDTO);

    List<FollowerDetailsDTO> searchUser(String username);

    Boolean removeUser(String userEmail);

    SellerDetailsDTO getSellerDetails(String sellerID);
}
