package Vortex.authservice.controller;

import Vortex.authservice.dto.request.UpdateUserPublicDetailsDTO;
import Vortex.authservice.dto.request.UserSignUpDTO;
import Vortex.authservice.dto.response.UserByEmailDTO;
import Vortex.authservice.service.UserService;
import Vortex.authservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("find_account_by_email")
    public ResponseEntity<StandardResponse> findAccountById(@RequestParam("email")String email){
        UserByEmailDTO user = userService.getUserbyEmail(email);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"User By Email : ",user),HttpStatus.OK
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
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
}
