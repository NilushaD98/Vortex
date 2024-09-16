package Vortex.authservice.service.Impl;

import Vortex.authservice.entity.User;
import Vortex.authservice.exceptions.UserAlreadyReportedException;
import Vortex.authservice.exceptions.UserNotFoundException;
import Vortex.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> byEmailEquals = userRepository.findByEmailEquals(email);
        if(!byEmailEquals.isPresent()){
            throw new UserNotFoundException();
        }else {
            return byEmailEquals.get();
        }
    }
}
