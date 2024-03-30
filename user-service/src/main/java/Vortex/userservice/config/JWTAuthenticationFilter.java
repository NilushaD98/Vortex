package Vortex.userservice.config;

import Vortex.userservice.service.IMPL.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;
        if(authHeader == null  || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
        }
        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(jwt);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            if(jwtService.isTokenExpired(jwt)){

                List<GrantedAuthority> authorities = new ArrayList<>();


            }
        }
    }

    // Once we get the token validate it.
        if (SecurityContextHolder.getContext().getAuthentication() == null) {

//            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

        // if token is valid configure Spring Security to manually set
        // authentication
        if (jwtTokenUtil.validateToken(jwtToken)) {

            List<GrantedAuthority> authorities = new ArrayList<>();
            JSONArray objects = new JSONArray(privilages);
            objects.forEach(o -> {
                authorities.add(new SimpleGrantedAuthority(o.toString()));
            });

//                SimpleGrantedAuthority
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    null, null, authorities);
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // After setting the Authentication in the context, we specify
            // that the current user is authenticated. So it passes the
            // Spring Security Configurations successfully.
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
}
