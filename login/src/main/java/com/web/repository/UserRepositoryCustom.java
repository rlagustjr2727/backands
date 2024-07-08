package com.web.repository; // 패키지 선언

public interface UserRepositoryCustom { // 사용자 정의 리포지토리 인터페이스 선언
    boolean existsByUserNickNameCustom(String userNickName); // userNickName으로 존재 여부를 확인하는 메소드 선언
}
