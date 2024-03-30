package Vortex.userservice.service.IMPL;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private Key getSigninKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public List<GrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractClaims(token);
        Collection<?> authorities = claims.get("authorities", Collection.class);
        return authorities.stream()
                .map(Object::toString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    private Claims extractClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }
}
