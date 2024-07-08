package com.web.service; // 패키지 선언

import org.springframework.beans.factory.annotation.Autowired; // Autowired 어노테이션을 임포트
import org.springframework.security.crypto.password.PasswordEncoder; // PasswordEncoder 인터페이스를 임포트
import org.springframework.stereotype.Service; // Service 어노테이션을 임포트
import org.springframework.transaction.annotation.Transactional; // Transactional 어노테이션을 임포트

import com.web.repository.UserRepository; // UserRepository 인터페이스를 임포트
import com.web.user.User; // User 클래스를 임포트

@Service // 이 클래스를 Spring Bean으로 등록
public class UserServiceImpl implements UserService { // UserService 인터페이스를 구현하는 클래스 선언

    @Autowired // UserRepository 빈을 자동 주입
    private UserRepository userRepository;

    @Autowired // PasswordEncoder 빈을 자동 주입
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional // 이 메소드를 트랜잭션으로 처리
    public void registerUser(User user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword())); // 비밀번호 암호화
        userRepository.save(user); // 사용자 정보를 저장
    }

    @Override
    public User findByUserIdAndUserPassword(String userId, String userPassword) {
        return userRepository.findByUserIdAndUserPassword(userId, userPassword); // 사용자 ID와 비밀번호로 사용자 찾기
    }

    @Override
    public boolean isUserIdExists(String userId) {
        return userRepository.existsByUserId(userId); // 사용자 ID가 존재하는지 확인
    }

    @Override
    public boolean isUserNickNameExists(String userNickName) {
        return userRepository.existsByUserNickNameCustom(userNickName); // 사용자 닉네임이 존재하는지 확인
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null); // 사용자 ID로 사용자 찾기, 없으면 null 반환
    }
}
