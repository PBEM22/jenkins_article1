package article1be.config;

// 1. 코드받기 (인증) 2. 엑세스 토큰 (권한)
// 3. 사용자 프로필 정보를 가져오고
// 4-1. 그 정보를 토대로 회원 가입을 자동으로 진행시키기 가능
// 4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집 주소), 백화점 -> (vip 등급, 일반 등급) 등 추가적인 구성이 필요

import article1be.user.service.NaverAndGoogleService;
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

    private final NaverAndGoogleService naverAndGoogleService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login") // login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행
                        .defaultSuccessUrl("/")
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/loginForm") // 구글 로그인이 완료된 뒤의 후처리가 필요. Tip. 코드 X (액세스 토큰 + 사용자 프로필 정보 O)
                        .userInfoEndpoint(user -> user.userService(naverAndGoogleService))
                )
                .build();
    }

}
