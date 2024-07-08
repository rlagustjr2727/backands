package com.web.repository; // 패키지 선언

import java.util.Optional; // Optional 클래스를 임포트
import org.springframework.data.jpa.repository.JpaRepository; // JpaRepository 인터페이스를 임포트
import org.springframework.stereotype.Repository; // Repository 어노테이션을 임포트
import com.web.user.User; // User 클래스를 임포트

@Repository // 이 인터페이스를 Spring Data JPA 리포지토리로 선언
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom { // User 엔티티와 관련된 데이터베이스 작업을 처리하는 리포지토리 인터페이스 선언
    boolean existsByUserId(String userId); // userId가 존재하는지 확인하는 메소드 선언
    User findByUserIdAndUserPassword(String userId, String userPassword); // userId와 userPassword로 User 엔티티를 찾는 메소드 선언
    Optional<User> findByUserName(String userName); // userName으로 User 엔티티를 찾는 메소드 선언, Optional로 반환
    User findByUserNickName(String userNickName); // userNickName으로 User 엔티티를 찾는 메소드 선언
}
