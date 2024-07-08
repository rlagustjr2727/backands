package com.web.config; // 패키지 선언

import java.util.Properties; // Properties 클래스를 임포트

import org.springframework.context.annotation.Bean; // Bean 어노테이션을 임포트
import org.springframework.context.annotation.Configuration; // Configuration 어노테이션을 임포트
import org.springframework.mail.javamail.JavaMailSender; // JavaMailSender 인터페이스를 임포트
import org.springframework.mail.javamail.JavaMailSenderImpl; // JavaMailSenderImpl 클래스를 임포트

@Configuration // 이 클래스를 설정 클래스임을 나타냄
public class MailConfig {
	
	@Bean(name = "googleMailSender") // googleMailSender라는 이름의 Bean을 생성
	public JavaMailSender googleMailSender() { // Google 메일 발송기를 구성하는 메소드
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl(); // JavaMailSenderImpl 객체 생성
		mailSender.setHost("smtp.gmail.com"); // 메일 서버 호스트를 설정
		mailSender.setPort(587); // 메일 서버 포트를 설정
		mailSender.setUsername("seungjaebaekmail@gmail.com"); // 이메일 사용자 이름 설정
		mailSender.setPassword("efycacxbbodaaved"); // 이메일 사용자 비밀번호 설정
		
		Properties props = mailSender.getJavaMailProperties(); // 메일 서버 속성을 가져옴
		props.put("mail.smtp.auth", "true"); // SMTP 인증을 활성화
		props.put("mail.smtp.starttls.enable", "true"); // STARTTLS를 활성화
		props.put("mail.smtp.startls.required", "true"); // STARTTLS를 필수로 설정
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // SSL 인증 서버 설정
		
		return mailSender; // JavaMailSender 객체를 반환
	}
	
	@Bean(name = "naverMailSender") // naverMailSender라는 이름의 Bean을 생성
	public JavaMailSender naverMailSender() { // Naver 메일 발송기를 구성하는 메소드
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl(); // JavaMailSenderImpl 객체 생성
		mailSender.setHost("smtp.naver.com"); // 메일 서버 호스트를 설정
		mailSender.setPort(465); // 메일 서버 포트를 설정
		mailSender.setUsername("baekseungjae1994@naver.com"); // 이메일 사용자 이름 설정
		mailSender.setPassword("baekseungjae"); // 이메일 사용자 비밀번호 설정
		
		Properties props = mailSender.getJavaMailProperties(); // 메일 서버 속성을 가져옴
		props.put("mail.smtp.auth", "true"); // SMTP 인증을 활성화
		props.put("mail.smtp.starttls.enable", "true"); // STARTTLS를 활성화
		props.put("mail.smtp.ssl.enable", "true"); // SSL을 활성화
		props.put("mail.smtp.starttls.required", "true"); // STARTTLS를 필수로 설정
		props.put("mail.smtp.ssl.trust", "smtp.naver.com"); // SSL 인증 서버 설정
		
		return mailSender; // JavaMailSender 객체를 반환
	}
	
}
