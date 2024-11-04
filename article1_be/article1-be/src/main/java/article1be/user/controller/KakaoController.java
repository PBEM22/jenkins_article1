package article1be.user.controller;

import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.user.dto.KakaoTokenDTO;
import article1be.user.dto.KakaoUserDTO;
import article1be.user.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    /* 인가 코드 받기 GET 요청 https://kauth.kakao.com/oauth/authorize */
    // 요청: https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}

    /* 회원이 소셜 로그인을 마치면 자동으로 실행되는 API
    *  인가 코드를 이용해 AccessToken 을 받고, 해당 토큰으로 사용자 정보를 가져온다. 사용자 정보를 이용하여 서비스에 회원가입한다.*/
    @ResponseBody
    @GetMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code) {

        try {
            log.info("인가 코드를 이용하여 AccessToken 발급, 인가 코드 : {}", code);
            KakaoTokenDTO kakaoToken = kakaoService.getKakaoAccessToken(code);

            log.info("토큰에 대한 정보 : {}", kakaoToken);
            KakaoUserDTO userInfo = kakaoService.getUserInfo(kakaoToken.getAccess_token());

            log.info("사용자 정보 요청 성공, 회원 정보 : {}", userInfo);

            return ResponseEntity.ok(userInfo);

        } catch (CustomException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}
