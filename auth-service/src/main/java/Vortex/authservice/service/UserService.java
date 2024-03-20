package Vortex.authservice.service;

import Vortex.authservice.dto.request.*;
import Vortex.authservice.dto.response.AuthResponseDTO;
import Vortex.authservice.dto.response.OtpResponse;
import Vortex.authservice.dto.response.UserByEmailDTO;

public interface UserService {
    AuthResponseDTO userSignUp(UserSignUpDTO userSignUpDTO);

    UserByEmailDTO getUserById(String userId);

    String updateUserDetails(UpdateUserPublicDetailsDTO updateUserPublicDetailsDTO);

    UserByEmailDTO getUserbyEmail(String email);

    OtpResponse sendOtpToEmail(String email);

    boolean checkOTP(CheckOTPDTO checkOTPDTO);

    AuthResponseDTO sellerSignUp(SellerSignUpDTO sellerSignUpDTO);
}
