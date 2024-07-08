package com.web.repository; // 패키지 선언

import org.springframework.data.jpa.repository.JpaRepository; // JpaRepository 인터페이스를 임포트

import com.web.board.Whisky; // Whisky 엔티티 클래스를 임포트

public interface WhiskyRepository extends JpaRepository<Whisky, Long> { // Whisky 엔티티와 관련된 데이터베이스 작업을 처리하는 리포지토리 인터페이스 선언
    // JpaRepository를 확장하여 기본적인 CRUD 작업을 지원
    // Long은 Whisky 엔티티의 ID 타입을 의미
}
