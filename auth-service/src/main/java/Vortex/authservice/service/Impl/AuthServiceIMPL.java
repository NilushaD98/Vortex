package Vortex.authservice.service.Impl;

import Vortex.authservice.dto.request.DefaultAuthenticationDTO;
import Vortex.authservice.dto.response.AuthResponseDTO;
import Vortex.authservice.entity.Token;
import Vortex.authservice.entity.User;
import Vortex.authservice.exceptions.UserNotFoundException;
import Vortex.authservice.repository.TokenRepository;
import Vortex.authservice.repository.UserRepository;
import Vortex.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceIMPL implements AuthService {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtServiceIMPL jwtService;
    @Override
    public AuthResponseDTO authenticate(DefaultAuthenticationDTO authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword())
        );
        Optional<User> user = userRepository.findByEmailEquals(authenticationRequest.getUsername());
        if(user.isPresent()){
            revokeAllUserToken(user.get());
            String access_token = jwtService.generateToken(user.get());
            Token token = new Token(access_token,user.get().getEmail());
            tokenRepository.save(token);
            return new AuthResponseDTO(
                    access_token,
                    jwtService.generateRefreshToken(user.get()),
                    user.get().getUserid(),
                    user.get().getRole().toString()
            );
        }else {
            throw new UserNotFoundException();
        }
    }
    private void revokeAllUserToken(User user){
        List<Token> tokens = tokenRepository.findTokensByUserEmailEquals(user.getEmail());
        if(tokens.isEmpty()){
            return;
        }
        tokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);

        });
        tokenRepository.saveAll(tokens);
    }
}
