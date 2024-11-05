package article1be.config;

// 1. 코드받기 (인증) 2. 엑세스 토큰 (권한)
// 3. 사용자 프로필 정보를 가져오고
// 4-1. 그 정보를 토대로 회원 가입을 자동으로 진행시키기 가능
// 4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집 주소), 백화점 -> (vip 등급, 일반 등급) 등 추가적인 구성이 필요

import article1be.user.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig { // 스프링 필터 역할

    private final SocialLoginService socialLoginService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/loginForm") // 프론트엔드 로그인 페이지로 설정 예정
                        .userInfoEndpoint(user -> user.userService(socialLoginService))
                )
                .build();
    }

}
