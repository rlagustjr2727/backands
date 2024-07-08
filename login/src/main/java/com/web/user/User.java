package com.web.user; // 패키지 선언

import java.util.Date; // Date 클래스를 임포트

import javax.persistence.Column; // Column 어노테이션을 임포트
import javax.persistence.Entity; // Entity 어노테이션을 임포트
import javax.persistence.Id; // Id 어노테이션을 임포트
import javax.persistence.Table; // Table 어노테이션을 임포트
import javax.persistence.Temporal; // Temporal 어노테이션을 임포트
import javax.persistence.TemporalType; // TemporalType 클래스를 임포트
import lombok.Getter; // Getter 어노테이션을 임포트
import lombok.Setter; // Setter 어노테이션을 임포트

@Getter // Lombok을 사용하여 getter 메소드 자동 생성
@Setter // Lombok을 사용하여 setter 메소드 자동 생성
@Entity // JPA 엔티티로 선언
@Table(name = "users") // 데이터베이스 테이블명 설정
public class User {

    @Id // 기본 키(primary key)로 선언
    @Column(name = "user_id") // 컬럼명 설정
    private String userId; // 사용자 ID

    @Column(name = "user_name", nullable = false) // 컬럼명과 nullable 속성 설정
    private String userName; // 사용자 이름

    @Column(name = "user_nickname", unique = true, nullable = false) // 컬럼명과 unique 및 nullable 속성 설정
    private String userNickName; // 사용자 닉네임

    @Column(name = "user_password", nullable = false) // 컬럼명과 nullable 속성 설정
    private String userPassword; // 사용자 비밀번호

    @Column(name = "user_email", nullable = false) // 컬럼명과 nullable 속성 설정
    private String userEmail; // 사용자 이메일

    @Column(name = "user_domain", nullable = false) // 컬럼명과 nullable 속성 설정
    private String userDomain; // 사용자 도메인

    @Column(name = "user_birth", nullable = false) // 컬럼명과 nullable 속성 설정
    @Temporal(TemporalType.DATE) // 날짜 타입 설정
    private Date userBirth; // 사용자 생년월일

    @Column(name = "user_phone_num", nullable = false) // 컬럼명과 nullable 속성 설정
    private String userPhoneNum; // 사용자 전화번호

    @Column(name = "user_profile_image", nullable = false) // 컬럼명과 nullable 속성 설정
    private String userProfileImage; // 사용자 프로필 이미지
}
