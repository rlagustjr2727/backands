package com.web.repository; // 패키지 선언

import javax.persistence.EntityManager; // EntityManager 클래스를 임포트
import javax.persistence.PersistenceContext; // PersistenceContext 어노테이션을 임포트
import javax.persistence.Query; // Query 클래스를 임포트

import org.springframework.stereotype.Repository; // Repository 어노테이션을 임포트

@Repository // 이 클래스를 리포지토리로 선언
public class UserRepositoryCustomImpl implements UserRepositoryCustom { // UserRepositoryCustom 인터페이스를 구현하는 클래스 선언

    @PersistenceContext // EntityManager를 자동 주입
    private EntityManager entityManager;

    @Override // UserRepositoryCustom 인터페이스의 메소드 구현을 나타냄
    public boolean existsByUserNickNameCustom(String userNickName) {
        try {
            String sql = "SELECT COUNT(u) FROM User u WHERE u.userNickName = :userNickName"; // User 엔티티에서 userNickName을 조건으로 개수를 세는 JPQL 쿼리
            Query query = entityManager.createQuery(sql); // EntityManager를 사용하여 쿼리 생성
            query.setParameter("userNickName", userNickName); // 쿼리 파라미터 설정
            Long count = (Long) query.getSingleResult(); // 쿼리 실행 후 결과를 Long 타입으로 반환
            return count > 0; // 개수가 0보다 큰지 확인하여 존재 여부 반환
        } catch (Exception e) { // 예외 처리
            e.printStackTrace(); // 예외 스택 트레이스 출력
            return false; // 예외 발생 시 false 반환
        }
    }
}
