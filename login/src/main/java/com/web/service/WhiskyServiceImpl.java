package com.web.service; // 패키지 선언

import java.util.List; // List 인터페이스를 임포트
import org.springframework.beans.factory.annotation.Autowired; // Autowired 어노테이션을 임포트
import org.springframework.stereotype.Service; // Service 어노테이션을 임포트

import com.web.board.Whisky; // Whisky 엔티티 클래스를 임포트
import com.web.repository.WhiskyRepository; // WhiskyRepository 인터페이스를 임포트

@Service // 이 클래스를 Spring 서비스 빈으로 선언
public class WhiskyServiceImpl implements WhiskyService { // WhiskyService 인터페이스를 구현하는 클래스 선언

    @Autowired // WhiskyRepository 빈을 자동 주입
    private WhiskyRepository whiskyRepository;

    @Override // 인터페이스 메소드 구현을 나타냄
    public List<Whisky> getAllWhiskies() {
        return whiskyRepository.findAll(); // 모든 위스키 목록을 반환
    }

    @Override // 인터페이스 메소드 구현을 나타냄
    public Whisky getWhiskyById(Long id) {
        return whiskyRepository.findById(id).orElse(null); // 특정 ID의 위스키를 반환, 없으면 null 반환
    }

    @Override // 인터페이스 메소드 구현을 나타냄
    public Whisky saveWhisky(Whisky whisky) {
        return whiskyRepository.save(whisky); // 새로운 위스키를 저장하거나 기존 위스키를 업데이트하고 반환
    }

    @Override // 인터페이스 메소드 구현을 나타냄
    public void deleteWhisky(Long id) {
        whiskyRepository.deleteById(id); // 특정 ID의 위스키를 삭제
    }
}
