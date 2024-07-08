package com.web.controller;

import org.springframework.beans.factory.annotation.Autowired; // Spring의 Autowired 어노테이션을 임포트
import org.springframework.util.StringUtils; // Spring의 StringUtils 유틸리티 클래스를 임포트
import org.springframework.web.bind.annotation.*; // 여러 웹 관련 어노테이션을 임포트
import org.springframework.web.multipart.MultipartFile; // MultipartFile 클래스를 임포트

import com.web.service.WhiskyService; // WhiskyService 인터페이스를 임포트
import com.web.board.Whisky; // Whisky 엔티티 클래스를 임포트

import java.io.IOException; // IO 예외 처리를 위한 클래스 임포트
import java.nio.file.*; // 파일 처리를 위한 NIO 패키지 임포트
import java.util.Date; // Date 클래스를 임포트
import java.util.List; // List 인터페이스를 임포트

@RestController // 해당 클래스를 REST 컨트롤러로 지정
@RequestMapping("/api/whiskies") // 기본 URL 경로를 "/api/whiskies"로 지정
public class WhiskyController {

    @Autowired // WhiskyService를 자동 주입
    private WhiskyService whiskyService;

    @GetMapping // HTTP GET 요청을 처리하는 메소드
    public List<Whisky> getAllWhiskies() {
        return whiskyService.getAllWhiskies(); // 모든 위스키 목록을 반환
    }

    @GetMapping("/{id}") // 특정 ID의 위스키를 조회하는 HTTP GET 요청을 처리하는 메소드
    public Whisky getWhiskyById(@PathVariable Long id) {
        return whiskyService.getWhiskyById(id); // ID에 해당하는 위스키를 반환
    }

    @PostMapping // 새로운 위스키를 생성하는 HTTP POST 요청을 처리하는 메소드
    public Whisky createWhisky(
            @RequestParam("whiskyName") String whiskyName, // whiskyName 파라미터를 받음
            @RequestParam("category") String category, // category 파라미터를 받음
            @RequestParam("imageFile") MultipartFile imageFile, // imageFile 파라미터를 받음
            @RequestParam("content") String content) { // content 파라미터를 받음
        
        // 이미지 파일을 저장할 경로 설정
        String uploadDir = "path/to/upload/directory/";
        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename()); // 파일 이름을 정리
        Path path = Paths.get(uploadDir + fileName); // 파일 경로를 설정

        try {
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING); // 파일을 지정된 경로에 저장
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file", e); // 예외 발생 시 런타임 예외 던짐
        }

        Whisky whisky = new Whisky(); // 새로운 Whisky 객체 생성
        whisky.setWboardWname(whiskyName); // 위스키 이름 설정
        whisky.setWboardCategory(category); // 카테고리 설정
        whisky.setWboardImage(fileName); // 이미지 파일 이름 설정
        whisky.setWboardContent(content); // 내용 설정
        whisky.setWboardDate(new Date()); // 작성일 설정

        return whiskyService.saveWhisky(whisky); // 위스키 객체를 저장하고 반환
    }

    @DeleteMapping("/{id}") // 특정 ID의 위스키를 삭제하는 HTTP DELETE 요청을 처리하는 메소드
    public void deleteWhisky(@PathVariable Long id) {
        whiskyService.deleteWhisky(id); // ID에 해당하는 위스키를 삭제
    }
}
