package com.web.board;

import java.util.Date; // Date 클래스를 임포트
import javax.persistence.Column; // JPA의 Column 어노테이션을 임포트
import javax.persistence.Entity; // JPA의 Entity 어노테이션을 임포트
import javax.persistence.GeneratedValue; // JPA의 GeneratedValue 어노테이션을 임포트
import javax.persistence.GenerationType; // JPA의 GenerationType 어노테이션을 임포트
import javax.persistence.Id; // JPA의 Id 어노테이션을 임포트
import javax.persistence.Table; // JPA의 Table 어노테이션을 임포트
import lombok.Getter; // Lombok의 Getter 어노테이션을 임포트
import lombok.Setter; // Lombok의 Setter 어노테이션을 임포트

@Getter // Lombok을 사용하여 getter 메소드를 자동으로 생성
@Setter // Lombok을 사용하여 setter 메소드를 자동으로 생성
@Entity // JPA Entity로 선언
@Table(name = "WBOARD") // 테이블 이름을 "WBOARD"로 지정
public class Whisky {
	
	@Id // 기본 키로 선언
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 전략을 IDENTITY로 설정
	@Column(name = "Wboard_SEQ", nullable = false) // 컬럼 이름을 "Wboard_SEQ"로 지정하고 null을 허용하지 않음
	private Long WboardSeq; // 게시판 글번호
	
	@Column(name = "Wboard_WNAME", nullable = false) // 컬럼 이름을 "Wboard_WNAME"로 지정하고 null을 허용하지 않음
	private String WboardWname; // 위스키 이름
	
	@Column(name = "Wboard_DATE", nullable = false) // 컬럼 이름을 "Wboard_DATE"로 지정하고 null을 허용하지 않음
	private Date WboardDate; // 작성일
	
	@Column(name = "Wboard_CATEGORY", nullable = false) // 컬럼 이름을 "Wboard_CATEGORY"로 지정하고 null을 허용하지 않음
	private String WboardCategory; // 게시판 선택(카테고리)
	
	@Column(name = "Wboard_IMAGE", nullable = false) // 컬럼 이름을 "Wboard_IMAGE"로 지정하고 null을 허용하지 않음
	private String WboardImage; // 게시판 이미지
	
	@Column(name = "Wboard_CONTENT", nullable = false) // 컬럼 이름을 "Wboard_CONTENT"로 지정하고 null을 허용하지 않음
	private String WboardContent; // 게시판 내용
	
}
