package com.web.security; // 패키지 선언

import com.web.user.User; // User 클래스를 임포트
import com.web.repository.UserRepository; // UserRepository 인터페이스를 임포트
import org.springframework.beans.factory.annotation.Autowired; // Autowired 어노테이션을 임포트
import org.springframework.security.core.userdetails.UserDetails; // UserDetails 인터페이스를 임포트
import org.springframework.security.core.userdetails.UserDetailsService; // UserDetailsService 인터페이스를 임포트
import org.springframework.security.core.userdetails.UsernameNotFoundException; // UsernameNotFoundException 클래스를 임포트
import org.springframework.stereotype.Service; // Service 어노테이션을 임포트

@Service // 이 클래스를 서비스 빈으로 선언
public class CustomUserDetailService implements UserDetailsService { // UserDetailsService 인터페이스를 구현하는 클래스 선언

    @Autowired // UserRepository 빈을 자동 주입
    private UserRepository userRepository;

    @Override // UserDetailsService 인터페이스의 메소드 구현을 나타냄
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId) // userId로 사용자 정보를 조회
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId)); // 사용자가 없으면 예외 발생
        return UserPrincipal.create(user); // UserPrincipal 객체를 생성하여 반환
    }
}
