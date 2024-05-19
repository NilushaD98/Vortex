package Vortex.userservice.config;

import Vortex.userservice.service.IMPL.JwtService;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(
                request.getServletPath().equals("/userservice/api/v1/user/initializeFollowingAndFollowersList")
        ){
            filterChain.doFilter(request,response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null){
            response.setStatus(401);
        }else {
            final String jwt;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(401);
                filterChain.doFilter(request, response);
            }
            jwt = authHeader.substring(7);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                if (!jwtService.isTokenExpired(jwt)) {
                    log.error(jwt);
                    log.error(jwtService.extractAuthorities(jwt).toString());
                    List<GrantedAuthority> authorities = jwtService.extractAuthorities(jwt);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            null,null,authorities
                    );
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }else {
                    response.setStatus(401);
                }
            }
            filterChain.doFilter(request,response);
        }
    }
}
