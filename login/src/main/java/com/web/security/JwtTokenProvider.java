package com.web.security; // 패키지 선언

import io.jsonwebtoken.*; // io.jsonwebtoken 패키지의 모든 클래스를 임포트
import org.springframework.beans.factory.annotation.Value; // Value 어노테이션을 임포트
import org.springframework.security.core.Authentication; // Authentication 클래스를 임포트
import org.springframework.stereotype.Component; // Component 어노테이션을 임포트

import java.util.Date; // Date 클래스를 임포트

@Component // 이 클래스를 Spring Bean으로 등록
public class JwtTokenProvider {

    @Value("${app.jwtSecret}") // application.properties에서 jwtSecret 값을 주입
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}") // application.properties에서 jwtExpirationInMs 값을 주입
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) { // JWT 토큰을 생성하는 메소드
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal(); // 인증 객체에서 사용자 정보를 가져옴

        Date now = new Date(); // 현재 시간
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs); // 토큰 만료 시간 계산

        return Jwts.builder() // JWT 토큰을 빌드
                .setSubject(userPrincipal.getUsername()) // 토큰의 주제를 사용자 이름으로 설정
                .setIssuedAt(now) // 토큰 발행 시간 설정
                .setExpiration(expiryDate) // 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // 서명 알고리즘과 비밀 키를 사용하여 서명
                .compact(); // 토큰을 문자열로 반환
    }

    public String getUserIdFromJWT(String token) { // JWT 토큰에서 사용자 ID를 추출하는 메소드
        Claims claims = Jwts.parser() // JWT 파서를 생성
                .setSigningKey(jwtSecret) // 서명 검증을 위한 비밀 키 설정
                .parseClaimsJws(token) // 토큰을 파싱하여 클레임을 추출
                .getBody(); // 클레임의 본문을 반환

        return claims.getSubject(); // 클레임의 주제를 반환 (사용자 ID)
    }

    public boolean validateToken(String authToken) { // JWT 토큰을 검증하는 메소드
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken); // 토큰을 파싱하고 서명을 검증
            return true; // 유효한 토큰이면 true 반환
        } catch (SignatureException ex) { // 서명 검증 실패
            //log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) { // 잘못된 토큰
            //log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) { // 만료된 토큰
            //log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) { // 지원되지 않는 토큰
            //log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) { // 토큰이 없거나 잘못된 경우
            //log.error("JWT claims string is empty.");
        }
        return false; // 유효하지 않은 토큰이면 false 반환
    }
}
