package article1be.user.controller;

import article1be.config.PrincipalDetails;
import article1be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SocialLoginController {

    private final UserRepository userRepository;

    // OAuth2 로그인 정보 조회 (구글, 네이버, 카카오 포함)
    @GetMapping("/oauth/login")
    public ResponseEntity<Map<String, Object>> getOAuthLoginInfo(Authentication authentication,
                                                                 @AuthenticationPrincipal OAuth2User oAuth) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String provider = oAuth2User.getAttributes().get("provider") != null ?
                oAuth2User.getAttributes().get("provider").toString() : "unknown";

        Map<String, Object> response = new HashMap<>();
        response.put("provider", provider);
        response.put("attributes", oAuth2User.getAttributes());

        return ResponseEntity.ok(response);
    }

    // 현재 로그인한 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<PrincipalDetails> getCurrentUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 사용자의 정보를 응답으로 반환
        return ResponseEntity.ok(principalDetails);
    }

}
