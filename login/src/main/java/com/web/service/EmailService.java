package com.web.service; // 패키지 선언

import java.util.Random; // Random 클래스를 임포트
import java.util.concurrent.ConcurrentHashMap; // ConcurrentHashMap 클래스를 임포트

import javax.mail.MessagingException; // MessagingException 클래스를 임포트
import javax.mail.internet.MimeMessage; // MimeMessage 클래스를 임포트

import org.springframework.beans.factory.annotation.Autowired; // Autowired 어노테이션을 임포트
import org.springframework.beans.factory.annotation.Qualifier; // Qualifier 어노테이션을 임포트
import org.springframework.mail.MailException; // MailException 클래스를 임포트
import org.springframework.mail.javamail.JavaMailSender; // JavaMailSender 인터페이스를 임포트
import org.springframework.mail.javamail.MimeMessageHelper; // MimeMessageHelper 클래스를 임포트
import org.springframework.stereotype.Service; // Service 어노테이션을 임포트

@Service // 이 클래스를 Spring Bean으로 등록
public class EmailService {

    @Autowired // googleMailSender 빈을 자동 주입
    @Qualifier("googleMailSender")
    private JavaMailSender googleMailSender;

    @Autowired // naverMailSender 빈을 자동 주입
    @Qualifier("naverMailSender")
    private JavaMailSender naverMailSender;

    private final ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<>(); // 이메일과 인증 코드를 매핑하는 맵
    private final ConcurrentHashMap<String, Boolean> verificationStatus = new ConcurrentHashMap<>(); // 이메일과 인증 상태를 매핑하는 맵
    private final Random random = new Random(); // 랜덤 숫자 생성을 위한 Random 객체

    public String sendVerificationCode(String to, String provider) { // 인증 코드를 이메일로 발송하는 메소드
        String code = generateVerificationCode(); // 인증 코드 생성
        try {
            MimeMessage mimeMessage = getMailSender(provider).createMimeMessage(); // 이메일 메시지 객체 생성
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8"); // 이메일 메시지 헬퍼 객체 생성
            String htmlMsg = "<h3>이메일 인증 코드</h3>" // 이메일 내용 설정
                    + "<p>안녕하세요.</p>"
                    + "<p>다음은 귀하의 이메일 인증 코드입니다. <strong>" + code + "</strong></p>"
                    + "<p>이 코드를 사용하여 이메일 인증을 완료하세요.</p>"
                    + "<p>감사합니다.</p>";
            helper.setText(htmlMsg, true); // HTML 형식의 이메일 설정
            helper.setTo(to); // 수신자 설정
            helper.setSubject("이메일 인증 코드"); // 이메일 제목 설정
            helper.setFrom(getSenderEmail(provider)); // 발신자 설정
            getMailSender(provider).send(mimeMessage); // 이메일 발송
            verificationCodes.put(to, code); // 이메일과 코드 매핑
            verificationStatus.put(to, false); // 초기 인증 상태는 false로 설정
        } catch (MailException | MessagingException e) { // 예외 처리
            e.printStackTrace(); // 예외 스택 트레이스 출력
            throw new IllegalStateException("인증 코드 발송 실패", e); // 예외 발생 시 메시지 출력
        }
        return code; // 생성된 인증 코드 반환
    }

    public boolean verifyCode(String email, String code) { // 이메일과 인증 코드를 검증하는 메소드
        String correctCode = verificationCodes.get(email); // 이메일에 매핑된 코드 가져오기
        if (correctCode != null && correctCode.equals(code)) { // 코드가 일치하면
            verificationStatus.put(email, true); // 인증 상태를 true로 설정
            return true; // 인증 성공 반환
        }
        return false; // 인증 실패 반환
    }

    public boolean isVerified(String email) { // 이메일 인증 여부를 확인하는 메소드
        return verificationStatus.getOrDefault(email, false); // 이메일 인증 상태 반환, 기본값은 false
    }

    private String generateVerificationCode() { // 인증 코드를 생성하는 메소드
        int code = random.nextInt(9000) + 1000; // 1000-9999 사이의 숫자 생성
        return String.format("%04d", code); // 4자리 숫자로 포맷팅하여 반환
    }

    private JavaMailSender getMailSender(String provider) { // 이메일 제공자에 따라 메일 발송기를 반환하는 메소드
        if ("google".equalsIgnoreCase(provider)) { // 제공자가 구글일 경우
            return googleMailSender;
        } else if ("naver".equalsIgnoreCase(provider)) { // 제공자가 네이버일 경우
            return naverMailSender;
        } else {
            throw new IllegalArgumentException("지원하지 않는 이메일 제공자입니다.: " + provider); // 지원하지 않는 제공자일 경우 예외 발생
        }
    }

    private String getSenderEmail(String provider) { // 이메일 제공자에 따라 발신자 이메일을 반환하는 메소드
        if ("google".equalsIgnoreCase(provider)) { // 제공자가 구글일 경우
            return "seungjaebaekmail@gmail.com";
        } else if ("naver".equalsIgnoreCase(provider)) { // 제공자가 네이버일 경우
            return "baekseungjae1994@naver.com";
        } else {
            throw new IllegalArgumentException("지원하지 않는 이메일 제공자입니다. " + provider); // 지원하지 않는 제공자일 경우 예외 발생
        }
    }
}
