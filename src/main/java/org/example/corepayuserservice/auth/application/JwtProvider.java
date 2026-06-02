package org.example.corepayuserservice.auth.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long expirationTime;

    // 생성자 주입: application.yml에 적어둔 설정값을 가져와서 미리 SecretKey 객체로 만들어둡니다.
    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration_time}") long expirationTime) {

        // 평문 시크릿 키를 JJWT가 요구하는 안전한 암호화 키 객체로 변환
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    //유저 정보를 받아서 JWT 토큰을 생성합니다.
    public String generateToken(Long userId, String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        // JJWT 0.12.6 최신 토큰 생성 문법 적용
        return Jwts.builder()
                .subject(String.valueOf(userId)) // 중요: 게이트웨이가 getSubject()로 userId를 꺼내므로 무조건 userId를 넣어야 함
                .claim("email", email)      // 페이로드에 이메일 정보 추가 (선택)
                .claim("role", role)        // 페이로드에 권한 정보 추가 (선택)
                .issuedAt(now)                   // 토큰 발급 시간 (iat)
                .expiration(expiryDate)          // 토큰 만료 시간 (exp)
                .signWith(secretKey)             // 만들어둔 시크릿 키로 서명
                .compact();                      // 최종 문자열로 압축하여 반환 ("eyJh...")
    }
}