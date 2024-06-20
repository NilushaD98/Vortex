package Vortex.authservice.controller;

import Vortex.authservice.dto.request.ChangePasswordDTO;
import Vortex.authservice.dto.request.CheckOTPDTO;
import Vortex.authservice.dto.request.DefaultAuthenticationDTO;
import Vortex.authservice.dto.response.AuthResponseDTO;
import Vortex.authservice.dto.response.OtpResponse;
import Vortex.authservice.service.AuthService;
import Vortex.authservice.service.UserService;
import Vortex.authservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vortexcoreservice/api/v1/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;
    private final UserService userService;
    @PostMapping("default_login")
    public ResponseEntity<StandardResponse> login(@RequestBody DefaultAuthenticationDTO authenticationRequest){
        AuthResponseDTO authResponseDTO = authService.authenticate(authenticationRequest);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Auth Response : ", authResponseDTO), HttpStatus.OK
        );
    }
    @GetMapping("send_otp_for_password_change")
    public ResponseEntity<StandardResponse> sendOTPtoEmailForPasswordChange(@RequestParam("email")String email){
        OtpResponse otpResponse = userService.sendOtpToEmail(email);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Otp Mail Send Status : ",otpResponse),HttpStatus.OK
        );
    }

    @PostMapping("check_otp_for_password_change")
    public ResponseEntity<StandardResponse> checkOTPForPasswordChange(@RequestBody CheckOTPDTO checkOTPDTO){
        boolean otpCheckStatus = userService.checkOTP(checkOTPDTO);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"OTP Checked Status : ", otpCheckStatus),HttpStatus.OK
        );
    }
    @PatchMapping("password_change_forget_password")
    public ResponseEntity<StandardResponse> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO){
        AuthResponseDTO passwordChangeStatus = authService.passwordChange(changePasswordDTO);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Password Change Status",passwordChangeStatus),HttpStatus.OK
        );
    }

//    @PostMapping("sign_in_with_google")
//    public ResponseEntity<StandardResponse> signInWithGoogle(@RequestBody )


}
