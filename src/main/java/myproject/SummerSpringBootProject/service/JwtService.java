package myproject.SummerSpringBootProject.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import myproject.SummerSpringBootProject.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final String secret_key = "66c139d77e0957e75fdafa4826ce2ad7409dbd5378cfd190bbc962b40e9f48e5";
    private final Duration jwtLifeTime = Duration.ofMinutes(30);

    public String extractUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String extractRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }

    private Claims getAllClaimsFromToken(String token){
        //parseClaimsJws() taip pat patikrina, ar tokenas validus, nepazeistas, nepadirbtas.
        return Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigninKey() {
        //atkoduoja secret key, kadangi uzkoduotas BASE64 koduote
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();

        String role = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
        claims.put("role", role);
        claims.put("id", user.getId());

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifeTime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
