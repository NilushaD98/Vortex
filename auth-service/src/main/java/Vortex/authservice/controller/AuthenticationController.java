package Vortex.authservice.controller;

import Vortex.authservice.dto.request.DefaultAuthenticationDTO;
import Vortex.authservice.dto.response.AuthResponseDTO;
import Vortex.authservice.service.AuthService;
import Vortex.authservice.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vortexcoreservice/api/v1/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;
    @PostMapping("default_login")
    public ResponseEntity<StandardResponse> login(@RequestBody DefaultAuthenticationDTO authenticationRequest){
        AuthResponseDTO authResponseDTO = authService.authenticate(authenticationRequest);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200,"Auth Response : ", authResponseDTO), HttpStatus.OK
        );
    }
}
