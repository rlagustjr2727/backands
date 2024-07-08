package com.web.controller;

import java.text.ParseException; // ParseException 클래스를 임포트
import java.text.SimpleDateFormat; // SimpleDateFormat 클래스를 임포트
import java.util.Date; // Date 클래스를 임포트
import java.util.HashMap; // HashMap 클래스를 임포트
import java.util.Map; // Map 인터페이스를 임포트

import javax.servlet.http.HttpServletRequest; // HttpServletRequest 클래스를 임포트
import javax.servlet.http.HttpServletResponse; // HttpServletResponse 클래스를 임포트
import javax.servlet.http.HttpSession; // HttpSession 클래스를 임포트

import org.springframework.beans.factory.annotation.Autowired; // Autowired 어노테이션을 임포트
import org.springframework.http.HttpStatus; // HttpStatus 클래스를 임포트
import org.springframework.http.ResponseEntity; // ResponseEntity 클래스를 임포트
import org.springframework.security.authentication.AuthenticationManager; // AuthenticationManager 인터페이스를 임포트
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // UsernamePasswordAuthenticationToken 클래스를 임포트
import org.springframework.security.core.Authentication; // Authentication 인터페이스를 임포트
import org.springframework.security.core.AuthenticationException; // AuthenticationException 클래스를 임포트
import org.springframework.ui.Model; // Model 인터페이스를 임포트
import org.springframework.web.bind.annotation.GetMapping; // GetMapping 어노테이션을 임포트
import org.springframework.web.bind.annotation.PathVariable; // PathVariable 어노테이션을 임포트
import org.springframework.web.bind.annotation.PostMapping; // PostMapping 어노테이션을 임포트
import org.springframework.web.bind.annotation.RequestBody; // RequestBody 어노테이션을 임포트
import org.springframework.web.bind.annotation.RequestMapping; // RequestMapping 어노테이션을 임포트
import org.springframework.web.bind.annotation.RequestParam; // RequestParam 어노테이션을 임포트
import org.springframework.web.bind.annotation.RestController; // RestController 어노테이션을 임포트

import com.web.security.JwtTokenProvider; // JwtTokenProvider 클래스를 임포트
import com.web.service.EmailService; // EmailService 클래스를 임포트
import com.web.service.UserService; // UserService 클래스를 임포트
import com.web.user.User; // User 클래스를 임포트

@RestController // 해당 클래스를 REST 컨트롤러로 지정
@RequestMapping("api/auth") // 기본 URL 경로를 "api/auth"로 지정
public class AuthController {
	
	@Autowired // UserService를 자동 주입
	private UserService userService;
	
	@Autowired // AuthenticationManager를 자동 주입
	private AuthenticationManager authenticationManager;
	
	@Autowired // JwtTokenProvider를 자동 주입
	private JwtTokenProvider tokenProvider;
	
	@Autowired // EmailService를 자동 주입
	private EmailService emailService;
	
	// 아이디 중복 확인
    @GetMapping("/checkUserId")
    public Map<String, Boolean> checkUserId(@RequestParam("userId") String userId) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = userService.isUserIdExists(userId);
        response.put("exists", exists);
        return response;
    }
	
	// 닉네임 중복 확인
	@GetMapping("/checkUserNickName")
	public Map<String, Boolean> checkUserNickName(@RequestParam("userNickName") String userNickName) {
		boolean exists = userService.isUserNickNameExists(userNickName);
		Map<String, Boolean> response = new HashMap<>();
		response.put("exists", exists);
		return response;
	}
	
	@PostMapping("/login") // 로그인 요청을 처리
	public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody Map<String, String> loginRequest) {
		Map<String, String> response = new HashMap<>();
		try {
			String userId = loginRequest.get("userId");
			String password = loginRequest.get("userPassword");
			
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userId, password)); // 사용자 인증
			String token = tokenProvider.generateToken(authentication); // JWT 토큰 생성
			
			response.put("token", token);
			response.put("message", "User authenticated successfully");
			return ResponseEntity.ok(response); // HTTP 200 상태 코드와 함께 응답
		} catch (AuthenticationException e) {
			response.put("message", "Invalid username or password");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // HTTP 401 상태 코드와 함께 응답
		} catch (Exception e) {
			response.put("message", "An error occurred during authentication");
			e.printStackTrace(); // 콘솔에 스택 트레이스 출력
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // HTTP 500 상태 코드와 함께 응답
		}
	}
	
	@PostMapping("/sendVerificationCode") // 인증 코드 전송 요청을 처리
	public Map<String, String> sendVerificationCode(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String provider = request.get("provider"); // 이메일 제공자
		emailService.sendVerificationCode(email, provider); // 인증 코드 전송
		
		Map<String, String> response = new HashMap<>();
		response.put("message", "인증번호가 이메일로 전송되었습니다.");
		return response;
	}
	
	@PostMapping("/verifyCode") // 인증 코드 확인 요청을 처리
	public Map<String, Boolean> verifyCode(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String code = request.get("code");
		boolean isValid = emailService.verifyCode(email, code); // 인증 코드 확인
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("isValid", isValid);
		return response;
	}
	
	@PostMapping("/checkVerificationStatus") // 인증 상태 확인 요청을 처리
	public Map<String, Boolean> checkVerificationStatus(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		boolean isVerified = emailService.isVerified(email); // 인증 상태 확인
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("isVerified", isVerified);
		return response;
	}
	
	// 회원가입 정보 입력
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Map<String, String> userRequest, Model model) throws ParseException {
		
		String userId = userRequest.get("userId");
		if (userId == null || userId.isEmpty()) {
			return ResponseEntity.badRequest().body("User ID is required"); // 유효성 검사
		}
		
		User user = new User();
		user.setUserId(userId);
		user.setUserName(userRequest.get("userName"));
		user.setUserNickName(userRequest.get("userNickName"));
		user.setUserPassword(userRequest.get("userPassword"));
		user.setUserEmail(userRequest.get("userEmail"));
		user.setUserDomain(userRequest.get("userDomain"));
		
		String userBirthStr = userRequest.get("userBirth");
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 형식 지정
		Date userBirth = dataFormat.parse(userBirthStr); // 문자열을 Date 객체로 변환
		user.setUserBirth(userBirth);
		
		user.setUserPhoneNum(userRequest.get("userPhoneNum"));
		user.setUserProfileImage(userRequest.getOrDefault("userProfileImage", "")); // 프로필 이미지가 없을 경우 기본값 사용
		
		if (!emailService.isVerified(user.getUserEmail())) {
			return ResponseEntity.badRequest().body("이메일 인증을 완료해야합니다."); // 이메일 인증 여부 확인
		}
		
		userService.registerUser(user); // 사용자 등록
		return ResponseEntity.ok("회원가입 성공");
	}
	
	@GetMapping("/user/{userId}") // 사용자 정보 요청을 처리
	public ResponseEntity<User> getUserById(@PathVariable String userId) {
		User user = userService.getUserById(userId);
		if (user != null) {
			return ResponseEntity.ok(user); // 사용자 정보 반환
		} else {
			return ResponseEntity.notFound().build(); // 사용자 정보가 없을 경우 404 응답
		}
	}
	
	// 로그아웃 엔드 포인트를 구현, 기본적으로 로그아웃 요청을 처리하고, 세션을 무효화하는 역할을 합니다.
	@PostMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // 세션 무효화
		}
		response.setStatus(HttpServletResponse.SC_OK); // HTTP 200 상태 코드 설정
	}
}
