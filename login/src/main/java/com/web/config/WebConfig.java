package com.web.config;

import org.springframework.context.annotation.Bean; // Spring의 Bean 어노테이션을 임포트
import org.springframework.context.annotation.Configuration; // Spring의 Configuration 어노테이션을 임포트
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Spring MVC의 CORS 설정을 위한 클래스 임포트
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry; // Spring MVC의 리소스 핸들러 설정을 위한 클래스 임포트
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Spring MVC 설정을 위한 인터페이스 임포트

@Configuration // 이 클래스가 설정 클래스임을 나타냄
public class WebConfig {

    @Bean // 이 메소드가 Spring Bean을 생성함을 나타냄
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() { // 익명 클래스 구현을 통해 WebMvcConfigurer 인터페이스를 구현
            @Override
            public void addCorsMappings(CorsRegistry registry) { // CORS 설정을 추가
                registry.addMapping("/api/**") // "/api/**" 경로에 대해 CORS를 허용
                        .allowedOrigins("http://localhost:3000") // CORS를 허용할 출처를 지정
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메소드를 지정
                        .allowedHeaders("*") // 모든 헤더를 허용
                        .allowCredentials(true); // 자격 증명을 허용
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) { // 정적 리소스 핸들러를 추가
                registry.addResourceHandler("/uploads/**") // "/uploads/**" 경로에 대해 리소스 핸들러를 설정
                        .addResourceLocations("file:path/to/upload/directory"); // 리소스가 위치한 디렉토리를 지정
            }
        };
    }
}
