package io.wisoft.vamos.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private final StringRedisTemplate redisTemplate;

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private Key key;


    /* Dependency Injection */
    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            StringRedisTemplate redisTemplate) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Bean 생성 후 DI 이후에 secret 값을 Base64 Decode 해서 key 변수에 할당
     */
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * authentication 객체의 권한 정보를 이용해서 토큰을 생성하는 createToken 메소드 추가
     * @param authentication
     * @return jwt token
     */
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        /* yml 파일에서 설정했던 만료시간을 설정 */
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * token 에 담겨있는 권한 정보를 이용해 Authentication 객체를 리턴하는 메소드
     * @param token
     * @return authentication
     */
    public Authentication getAuthentication(String token) {

        /* token 으로 클레임을 생성 */
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        /* 클레임을 이용해 유저 객체를 만들어서 최종적으로 Authentication 객체를 리턴 */
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰의 유효성 검증을 수행
     * @param token
     * @return 문제가 없으면 true 반환, 있으면 false 반환
     */
    public boolean validateToken(String token, ServletRequest request) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        try {
            if (token == null) {
                httpServletRequest.setAttribute("exception", ErrorCode.NON_TOKEN.getCode());
                logger.info("헤더정보에 토큰이 존재하지 않습니다.");
                return false;
            }

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            if (redisTemplate.opsForValue().get(token) != null) {
                httpServletRequest.setAttribute("exception", ErrorCode.LOGOUT_TOKEN.getCode());
                logger.info("로그아웃된 토큰 입니다.");
                return false;
            }
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            httpServletRequest.setAttribute("exception", ErrorCode.MALFORMED_TOKEN.getCode());
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            httpServletRequest.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getCode());
            logger.info("만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            httpServletRequest.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.getCode());
            logger.info("지원되지 않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            httpServletRequest.setAttribute("exception", ErrorCode.ILLEGAL_TOKEN.getCode());
            logger.info("JWT 이 잘못되었습니다.");
        }
        return false;
    }
}

