package com.web.security; // 패키지 선언

import com.web.user.User; // User 클래스를 임포트
import org.springframework.security.core.GrantedAuthority; // GrantedAuthority 인터페이스를 임포트
import org.springframework.security.core.userdetails.UserDetails; // UserDetails 인터페이스를 임포트

import java.util.Collection; // Collection 인터페이스를 임포트

public class UserPrincipal implements UserDetails { // UserDetails 인터페이스를 구현하는 UserPrincipal 클래스 선언
    private String id; // 사용자 ID를 저장할 필드
    private String password; // 사용자 비밀번호를 저장할 필드

    public UserPrincipal(String id, String password) { // 생성자
        this.id = id; // 사용자 ID를 초기화
        this.password = password; // 사용자 비밀번호를 초기화
    }

    public static UserPrincipal create(User user) { // User 객체를 UserPrincipal 객체로 변환하는 정적 메소드
        return new UserPrincipal(user.getUserId(), user.getUserPassword()); // UserPrincipal 객체 생성 및 반환
    }

    @Override
    public String getUsername() { // UserDetails 인터페이스의 getUsername 메소드 구현
        return id; // 사용자 ID를 반환
    }

    @Override
    public String getPassword() { // UserDetails 인터페이스의 getPassword 메소드 구현
        return password; // 사용자 비밀번호를 반환
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // UserDetails 인터페이스의 getAuthorities 메소드 구현
        return null; // 권한 정보를 반환 (현재 null 반환)
    }

    @Override
    public boolean isAccountNonExpired() { // UserDetails 인터페이스의 isAccountNonExpired 메소드 구현
        return true; // 계정이 만료되지 않았음을 반환
    }

    @Override
    public boolean isAccountNonLocked() { // UserDetails 인터페이스의 isAccountNonLocked 메소드 구현
        return true; // 계정이 잠기지 않았음을 반환
    }

    @Override
    public boolean isCredentialsNonExpired() { // UserDetails 인터페이스의 isCredentialsNonExpired 메소드 구현
        return true; // 자격 증명이 만료되지 않았음을 반환
    }

    @Override
    public boolean isEnabled() { // UserDetails 인터페이스의 isEnabled 메소드 구현
        return true; // 계정이 활성화되었음을 반환
    }
}
