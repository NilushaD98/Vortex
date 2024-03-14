package Vortex.authservice.service.Impl;

import Vortex.authservice.dto.request.DefaultAuthenticationDTO;
import Vortex.authservice.dto.request.UpdateUserPublicDetailsDTO;
import Vortex.authservice.dto.request.UserSignUpDTO;
import Vortex.authservice.dto.response.AuthResponseDTO;
import Vortex.authservice.dto.response.UserByEmailDTO;
import Vortex.authservice.entity.User;
import Vortex.authservice.entity.UserPublicDetails;
import Vortex.authservice.exceptions.UserAlreadyReportedException;
import Vortex.authservice.exceptions.UserNotFoundException;
import Vortex.authservice.repository.UserPublicDetailsRepository;
import Vortex.authservice.repository.UserRepository;
import Vortex.authservice.service.AuthService;
import Vortex.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static Vortex.authservice.enums.Roles.USER;

@Service
@RequiredArgsConstructor
public class UserServiceIMPL implements UserService{

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final UserPublicDetailsRepository userPublicDetailsRepository;

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
}
