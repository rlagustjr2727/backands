package com.web.service; // 패키지 선언

import com.web.user.User; // User 클래스를 임포트

public interface UserService { // UserService 인터페이스 선언
	
	public void registerUser(User user); // 사용자 등록 메소드 선언

	public User findByUserIdAndUserPassword(String userId, String userPassword); // 사용자 ID와 비밀번호로 사용자 찾기 메소드 선언
	
	boolean isUserIdExists(String userId); // 사용자 ID가 존재하는지 확인하는 메소드 선언
	
	boolean isUserNickNameExists(String userNickName); // 사용자 닉네임이 존재하는지 확인하는 메소드 선언
	
	User getUserById(String userId); // 사용자 ID로 사용자 찾기 메소드 선언
}
