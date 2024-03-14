package Vortex.authservice.service;

import Vortex.authservice.dto.request.DefaultAuthenticationDTO;
import Vortex.authservice.dto.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO authenticate(DefaultAuthenticationDTO authenticationRequest);
}
