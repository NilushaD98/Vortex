package Vortex.authservice.service.Impl;

import Vortex.authservice.dto.request.*;
import Vortex.authservice.dto.response.AuthResponseDTO;
import Vortex.authservice.dto.response.OtpResponse;
import Vortex.authservice.dto.response.UserByEmailDTO;
import Vortex.authservice.entity.OTP;
import Vortex.authservice.entity.SellerDetails;
import Vortex.authservice.entity.User;
import Vortex.authservice.entity.UserPublicDetails;
import Vortex.authservice.enums.Roles;
import Vortex.authservice.exceptions.EmailSenderErrorResponse;
import Vortex.authservice.exceptions.UserAlreadyReportedException;
import Vortex.authservice.exceptions.UserNotFoundException;
import Vortex.authservice.repository.OTPRepository;
import Vortex.authservice.repository.SellerDetailsRepository;
import Vortex.authservice.repository.UserPublicDetailsRepository;
import Vortex.authservice.repository.UserRepository;
import Vortex.authservice.service.AuthService;
import Vortex.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static Vortex.authservice.enums.Roles.USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceIMPL implements UserService{

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final UserPublicDetailsRepository userPublicDetailsRepository;
    private final JavaMailSender mailSender;
    private final OTPRepository otpRepository;
    private final SellerDetailsRepository sellerDetailsRepository;


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
            AuthResponseDTO authenticateResponse = authService.authenticate(new DefaultAuthenticationDTO(
                    userSignUpDTO.getEmail(),
                    userSignUpDTO.getPassword()
            ));
            return authenticateResponse;
        }
    }
    @Override
    public UserByEmailDTO getUserById(String userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()){

        }else {
            throw new UserNotFoundException();
        }
        return null;
    }
    @Override
    public String updateUserDetails(UpdateUserPublicDetailsDTO updateUserPublicDetailsDTO) {
        UserPublicDetails userPublicDetails = new UserPublicDetails(
                updateUserPublicDetailsDTO.getUserId(),
                updateUserPublicDetailsDTO.getProfilePhotoURL(),
                updateUserPublicDetailsDTO.getBio()
        );
        return userPublicDetailsRepository.save(userPublicDetails)+" updated";
    }

    @Override
    public UserByEmailDTO getUserbyEmail(String email) {
        Optional<User> byEmailEquals = userRepository.findByEmailEquals(email);
        if(byEmailEquals.isPresent()){
            User user = byEmailEquals.get();
            UserPublicDetails userPublicDetails = userPublicDetailsRepository.findByUserId(user.getUserid());
            return new UserByEmailDTO(
                    userPublicDetails.getProfilePhotoURL(),
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
        boolean vortexOtp = mailSender(email, "Vortex OTP", "V - " + Integer.toString(otp));
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
        log.info("Expired OTP Deleted");
    }
}
