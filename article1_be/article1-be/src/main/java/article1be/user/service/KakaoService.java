package article1be.user.service;

import article1be.common.aggregate.entity.*;
import article1be.user.dto.KakaoAccount;
import article1be.user.dto.KakaoTokenDTO;
import article1be.user.dto.KakaoUserDTO;
import article1be.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final WebClient webClient;
    private final UserRepository userRepository;

    private static final String GRANT_TYPE = "authorization_code";
    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    @Value("${kakao.api.client_id}")
    private String CLIENT_ID;
    @Value("${kakao.api.redirect_uri}")
    private String REDIRECT_URI;

    /* 토큰 받기 : POST 요청 https://kauth.kakao.com/oauth/token*/
    public KakaoTokenDTO getKakaoAccessToken(String code) {
        String uri = TOKEN_URI +
                "?grant_type=" + GRANT_TYPE + "&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&code=" + code;
        System.out.println("POST 요청 uri : " + uri);

        Flux<KakaoTokenDTO> response = webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(KakaoTokenDTO.class);

        return response.blockFirst();
    }

    /* 사용자 정보 가져오기 : GET 요청 https://kapi.kakao.com/v2/user/me */
    public KakaoUserDTO getUserInfo(String accessToken) {

        String uri = USER_INFO_URI;

        Flux<KakaoUserDTO> response = webClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToFlux(KakaoUserDTO.class);

        KakaoUserDTO userInfo = response.blockFirst();
        KakaoAccount kakaoAccount = userInfo.getKakao_account();

        // 회원가입 여부 확인 후 새로운 회원 저장
        if (!userRepository.existsByUserId(userInfo.getKakao_account().getEmail())) {
            createUser(kakaoAccount);
        }

        return userInfo;
    }

    @Transactional
    public void createUser(KakaoAccount kakaoAccount) {

        // 생년월일 변환
        LocalDate birthDate = null;

        if (kakaoAccount.getBirthyear() != null && kakaoAccount.getBirthday() != null) {
            String birthYear = kakaoAccount.getBirthyear();
            String birthDay = kakaoAccount.getBirthday(); // MMDD 형식

            int month = Integer.parseInt(birthDay.substring(0, 2)); // MM
            int day = Integer.parseInt(birthDay.substring(2, 4));   // DD

            birthDate = LocalDate.of(Integer.parseInt(birthYear), month, day);
        }

        // 성별 ENUM 타입으로 변환
        UserGender gender = null;
        if ("male".equalsIgnoreCase(kakaoAccount.getGender())) {
            gender = UserGender.MALE;
        } else if ("female".equalsIgnoreCase(kakaoAccount.getGender())) {
            gender = UserGender.FEMALE;
        }

        User user = User.builder()
                 .socialSite(UserSocialSite.KAKAO)
                 .id(kakaoAccount.getEmail())
                 .name(kakaoAccount.getName())
                 .phoneNum(kakaoAccount.getPhone_number())
                 .birthDate(birthDate)
                 .gender(gender)
                 .state(UserState.ACTIVE)
                 .userAuth(UserAuth.USER)
                 .build();

        userRepository.save(user);
        log.info("새로운 회원 저장 완료");
    }

}
