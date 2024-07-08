package com.web.security; // 패키지 선언

import org.springframework.beans.factory.annotation.Autowired; // Autowired 어노테이션을 임포트
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // UsernamePasswordAuthenticationToken 클래스를 임포트
import org.springframework.security.core.context.SecurityContextHolder; // SecurityContextHolder 클래스를 임포트
import org.springframework.security.core.userdetails.UserDetails; // UserDetails 인터페이스를 임포트
import org.springframework.security.core.userdetails.UserDetailsService; // UserDetailsService 인터페이스를 임포트
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // WebAuthenticationDetailsSource 클래스를 임포트
import org.springframework.stereotype.Component; // Component 어노테이션을 임포트
import org.springframework.web.filter.OncePerRequestFilter; // OncePerRequestFilter 클래스를 임포트

import javax.servlet.FilterChain; // FilterChain 인터페이스를 임포트
import javax.servlet.ServletException; // ServletException 클래스를 임포트
import javax.servlet.http.HttpServletRequest; // HttpServletRequest 클래스를 임포트
import javax.servlet.http.HttpServletResponse; // HttpServletResponse 클래스를 임포트
import java.io.IOException; // IOException 클래스를 임포트

@Component // 이 클래스를 Spring Bean으로 등록
public class JwtAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter를 상속받아 요청당 한 번만 실행되는 필터 구현

    @Autowired // JwtTokenProvider 빈을 자동 주입
    private JwtTokenProvider tokenProvider;

    @Autowired // UserDetailsService 빈을 자동 주입
    private UserDetailsService customUserDetailsService;

    @Override // doFilterInternal 메소드 오버라이드
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request); // 요청에서 JWT 토큰을 가져옴

        if (jwt != null && tokenProvider.validateToken(jwt)) { // JWT 토큰이 유효한지 확인
            String userId = tokenProvider.getUserIdFromJWT(jwt); // JWT 토큰에서 사용자 ID를 추출

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId); // 사용자 ID로 사용자 세부 정보를 로드
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // 인증 객체 생성
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 요청 세부 정보를 설정

            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder에 인증 객체 설정
        }

        filterChain.doFilter(request, response); // 다음 필터로 요청과 응답을 전달
    }

    private String getJwtFromRequest(HttpServletRequest request) { // 요청에서 JWT 토큰을 추출하는 메소드
        String bearerToken = request.getHeader("Authorization"); // 요청 헤더에서 Authorization 헤더 값을 가져옴
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) { // Bearer 토큰인지 확인
            return bearerToken.substring(7); // "Bearer " 문자열 이후의 토큰 부분을 반환
        }
        return null; // 유효한 토큰이 없으면 null 반환
    }
}
