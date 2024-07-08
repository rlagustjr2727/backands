package com.web.config; // 패키지 선언

import org.springframework.beans.factory.annotation.Autowired; // Autowired 어노테이션을 임포트
import org.springframework.context.annotation.Bean; // Bean 어노테이션을 임포트
import org.springframework.context.annotation.Configuration; // Configuration 어노테이션을 임포트
import org.springframework.security.authentication.AuthenticationManager; // AuthenticationManager 클래스를 임포트
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; // AuthenticationManagerBuilder 클래스를 임포트
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // HttpSecurity 클래스를 임포트
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // EnableWebSecurity 어노테이션을 임포트
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter; // WebSecurityConfigurerAdapter 클래스를 임포트
import org.springframework.security.config.http.SessionCreationPolicy; // SessionCreationPolicy 클래스를 임포트
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // BCryptPasswordEncoder 클래스를 임포트
import org.springframework.security.crypto.password.PasswordEncoder; // PasswordEncoder 인터페이스를 임포트
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // UsernamePasswordAuthenticationFilter 클래스를 임포트

import com.web.security.CustomUserDetailService; // CustomUserDetailService 클래스를 임포트
import com.web.security.JwtAuthenticationEntryPoint; // JwtAuthenticationEntryPoint 클래스를 임포트
import com.web.security.JwtAuthenticationFilter; // JwtAuthenticationFilter 클래스를 임포트

@Configuration // 이 클래스를 설정 클래스로 지정
@EnableWebSecurity // Spring Security 설정을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter { // WebSecurityConfigurerAdapter를 상속받아 Spring Security 설정을 구성

    @Autowired // CustomUserDetailService를 자동 주입
    private CustomUserDetailService userDetailService;

    @Autowired // JwtAuthenticationEntryPoint를 자동 주입
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired // JwtAuthenticationFilter를 자동 주입
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean // PasswordEncoder 빈을 생성
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder를 사용하여 비밀번호를 암호화
    }

    @Override // AuthenticationManagerBuilder를 구성하는 메소드
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
            .userDetailsService(userDetailService) // CustomUserDetailService를 사용하여 사용자 정보를 로드
            .passwordEncoder(passwordEncoder()); // BCryptPasswordEncoder를 사용하여 비밀번호를 암호화
    }

    @Bean // AuthenticationManager 빈을 생성
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean(); // AuthenticationManager 빈을 반환
    }

    @Override // HttpSecurity를 구성하는 메소드
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and().csrf().disable() // CORS 설정을 활성화하고 CSRF 보호를 비활성화
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and() // 인증되지 않은 접근에 대한 처리
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // 세션을 상태 없이 관리
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll() // /api/auth/** 경로는 인증 없이 접근 허용
                .anyRequest().authenticated(); // 그 외의 모든 요청은 인증 필요

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 추가
    }
}
