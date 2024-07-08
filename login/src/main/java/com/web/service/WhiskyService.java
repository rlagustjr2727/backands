package com.web.service; // 패키지 선언

import com.web.board.Whisky; // Whisky 엔티티 클래스를 임포트

import java.util.List; // List 인터페이스를 임포트

public interface WhiskyService { // WhiskyService 인터페이스 선언
    List<Whisky> getAllWhiskies(); // 모든 위스키 목록을 반환하는 메소드 선언
    Whisky getWhiskyById(Long id); // 특정 ID의 위스키를 반환하는 메소드 선언
    Whisky saveWhisky(Whisky whisky); // 새로운 위스키를 저장하거나 기존 위스키를 업데이트하는 메소드 선언
    void deleteWhisky(Long id); // 특정 ID의 위스키를 삭제하는 메소드 선언
}
