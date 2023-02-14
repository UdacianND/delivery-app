package uz.md.shopapp.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.md.shopapp.domain.User;

import java.security.Key;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.access.key}")
    private String JWT_SECRET_KEY_FOR_ACCESS_TOKEN;

    @Value("${jwt.access.expiration-time}")
    private Long JWT_EXPIRED_TIME_FOR_ACCESS_TOKEN;

    @Value("${jwt.refresh.key}")
    private String JWT_SECRET_KEY_FOR_REFRESH_TOKEN;

    @Value("${jwt.refresh.expiration-time}")
    private Long JWT_EXPIRED_TIME_FOR_REFRESH_TOKEN;

    public String generateAccessToken(User user, Timestamp issuedAt) {
        Timestamp expireDate = new Timestamp(System.currentTimeMillis() + JWT_EXPIRED_TIME_FOR_ACCESS_TOKEN);
        String userId = String.valueOf(user.getId());
        String generateUuid = hideUserId(userId);
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(generateUuid)
                .setIssuedAt(issuedAt)
                .setExpiration(expireDate)
                .signWith(getSignInKey(JWT_SECRET_KEY_FOR_ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateRefreshToken(User user) {
        Timestamp issuedAt = new Timestamp(System.currentTimeMillis());
        Timestamp expireDate = new Timestamp(System.currentTimeMillis() + JWT_EXPIRED_TIME_FOR_REFRESH_TOKEN);

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getId().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expireDate)
                .signWith(getSignInKey(JWT_SECRET_KEY_FOR_REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String token, boolean accessToken) {
        try {
            checkToken(token, accessToken);
            return true;
        } catch (Exception ex) {
            log.error(Arrays.toString(ex.getStackTrace()));
            return false;
        }
    }

    public void checkToken(String token, boolean accessToken) {
        Jwts.parserBuilder()
                .setSigningKey(getSignInKey(accessToken
                        ? JWT_SECRET_KEY_FOR_ACCESS_TOKEN
                        : JWT_SECRET_KEY_FOR_REFRESH_TOKEN))
                .build()
                .parseClaimsJws(token);
    }

    public String extractUserId(String token, boolean isAccessToken) {
        String s = extractClaim(token, Claims::getSubject, isAccessToken);
        return showUserId(s);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isAccessToken) {
        final Claims claims = extractAllClaims(token, isAccessToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, boolean isAccessToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey(isAccessToken
                        ? JWT_SECRET_KEY_FOR_ACCESS_TOKEN
                        : JWT_SECRET_KEY_FOR_REFRESH_TOKEN))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private String hideUserId(String userId) {
        String generatingUUID = String.valueOf(UUID.randomUUID());
        String substring = generatingUUID.substring(0, 18);
        String concat = substring.concat("-");
        String concat1 = concat.concat(userId);
        String substring1 = generatingUUID.substring(18);
        return concat1.concat(substring1);
    }

    private String showUserId(String concat) {
        return concat.substring(19, 55);
    }
}
