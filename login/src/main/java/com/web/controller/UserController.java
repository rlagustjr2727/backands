package com.web.controller; // 패키지 선언

import java.text.ParseException; // ParseException 클래스를 임포트
import java.util.Date; // Date 클래스를 임포트
import java.util.Map; // Map 인터페이스를 임포트
import javax.servlet.http.HttpServletRequest; // HttpServletRequest 클래스를 임포트
import javax.servlet.http.HttpSession; // HttpSession 클래스를 임포트
import org.springframework.beans.factory.annotation.Autowired; // Autowired 어노테이션을 임포트
import org.springframework.format.annotation.DateTimeFormat; // DateTimeFormat 어노테이션을 임포트
import org.springframework.http.ResponseEntity; // ResponseEntity 클래스를 임포트
import org.springframework.validation.annotation.Validated; // Validated 어노테이션을 임포트
import org.springframework.web.bind.annotation.*; // 여러 웹 관련 어노테이션을 임포트
import com.web.service.UserService; // UserService 클래스를 임포트
import com.web.user.User; // User 클래스를 임포트

@Validated // 이 클래스에서 입력 값 검증을 활성화
@RestController // 이 클래스를 REST 컨트롤러로 선언
@RequestMapping("/api/users") // 기본 URL 경로를 "/api/users"로 지정
public class UserController {

    @Autowired // UserService 빈을 자동 주입
    private UserService userService;

    @PostMapping("/login") // 로그인 요청을 처리하는 POST 메소드
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        try {
            String userId = loginRequest.get("userId"); // 요청에서 userId를 가져옴
            String userPassword = loginRequest.get("userPassword"); // 요청에서 userPassword를 가져옴
            User user = userService.findByUserIdAndUserPassword(userId, userPassword); // 사용자 조회
            if (user != null) { // 사용자가 존재하는 경우
                session.setAttribute("userId", user.getUserId()); // 세션에 userId를 저장
                return ResponseEntity.ok(Map.of("message", "Login success")); // 성공 응답 반환
            } else { // 사용자가 존재하지 않는 경우
                return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password")); // 인증 실패 응답 반환
            }
        } catch (NumberFormatException e) { // NumberFormatException 발생 시
            return ResponseEntity.status(400).body(Map.of("message", "Invalid user ID format")); // 잘못된 형식 응답 반환
        }
    }

    @PostMapping("/register") // 회원가입 요청을 처리하는 POST 메소드
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        userService.registerUser(user); // 사용자 등록
        return ResponseEntity.ok(Map.of("message", "Register success")); // 성공 응답 반환
    }

    @GetMapping("/{userId}") // 특정 사용자 조회 요청을 처리하는 GET 메소드
    public ResponseEntity<User> getUserId(@PathVariable String userId) {
        User user = userService.getUserById(userId); // 사용자 조회
        if (user != null) { // 사용자가 존재하는 경우
            return ResponseEntity.ok(user); // 사용자 정보 반환
        } else { // 사용자가 존재하지 않는 경우
            return ResponseEntity.notFound().build(); // 404 응답 반환
        }
    }

    @PostMapping("/logout") // 로그아웃 요청을 처리하는 POST 메소드
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        if (session != null) { // 세션이 존재하는 경우
            session.invalidate(); // 세션 무효화
        }
        return ResponseEntity.ok(Map.of("message", "Logout success")); // 성공 응답 반환
    }
}
