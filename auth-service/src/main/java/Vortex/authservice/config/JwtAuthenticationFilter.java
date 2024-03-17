package Vortex.authservice.config;

import Vortex.authservice.repository.TokenRepository;
import Vortex.authservice.service.Impl.JwtServiceIMPL;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceIMPL jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(
                request.getServletPath().equals("/vortexcoreservice/api/v1/user/user_sign_up") ||
                        request.getServletPath().equals("/vortexcoreservice/api/v1/default_login") ||
                        request.getServletPath().equals("/vortexcoreservice/api/v1/user/find_account_by_email") ||
                        request.getServletPath().equals("/vortexcoreservice/api/v1/send_otp_for_password_change") ||
                        request.getServletPath().equals("/vortexcoreservice/api/v1/check_otp_for_password_change") ||
                        request.getServletPath().equals("/vortexcoreservice/api/v1/password_change_forget_password")

        ){
            filterChain.doFilter(request,response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(jwt);
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
            boolean isTokenValid = tokenRepository.findTokensByToken(jwt).map(token -> !token.isExpired() && !token.isRevoked()).orElse(false);
            if(jwtService.isValidToken(jwt,userDetails) && isTokenValid){
                UsernamePasswordAuthenticationToken token  = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        filterChain.doFilter(request,response);
    }
}
