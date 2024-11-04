package article1be.user.service;

import article1be.config.GoogleUserInfo;
import article1be.config.NaverUserInfo;
import article1be.config.OAuth2UserInfo;
import article1be.config.PrincipalDetails;
import article1be.user.entity.*;
import article1be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverAndGoogleService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료 시, @AuthenticationPrincipal 어노테이션 생성
    @Override // 후처리 기능
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // getClientRegistration -> registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
        System.out.println("getClientRegistration = " + userRequest.getClientRegistration()); // 서버의 기본 정보를 출력
        System.out.println("getAccessToken = " + userRequest.getAccessToken().getTokenValue()); // 토큰 값
        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료-> code 리턴 (OAuth-Client 라이브러리) -> AccessToken 요청
        // 위까지가 userRequest 정보

        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필 받기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("getAttributes = " + oAuth2User.getAttributes()); // 회원 정보 출력

        // 회원 가입을 강제로 진행해볼 예정
        OAuth2UserInfo oAuth2Userinfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");

            oAuth2Userinfo  = new GoogleUserInfo(oAuth2User.getAttributes()); // GoogleUserInfo에 요청
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");

            oAuth2Userinfo  = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response")); // NaverUserInfo에 요청
        } else System.out.println("우리는 네이버와 구글만 지원해요.");

        String userSocialSite = oAuth2Userinfo.getGetProvider().toUpperCase(); // google, naver
        String userId = "";

        if(userSocialSite.equals("NAVER")) {
            userId = userSocialSite + '_' + oAuth2Userinfo.getProviderId();
        } else userId = oAuth2Userinfo.getProviderId();

        String userName = oAuth2Userinfo.getName();
        String userNickname = "회원 별명";
        String userPhoneNum = "";

        if(userSocialSite.equals("NAVER")) {
            userPhoneNum = oAuth2Userinfo.getMobile();
        } else userPhoneNum = "회원 휴대 전화 번호";

        String birthYear = "";
        String birthDay = "";
        String birthDate = "";
        LocalDate userBirthDate;

        if(userSocialSite.equals("NAVER")) {
            birthYear = oAuth2Userinfo.getBirthyear();
            birthDay = oAuth2Userinfo.getBirthday();
            birthDate = birthYear + "-" + birthDay;
            userBirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else userBirthDate = LocalDate.parse("0000-00-00");

        String userGender = "";

        if(userSocialSite.equals("NAVER")) {
            if(oAuth2Userinfo.getGender().charAt(0) == 'M') {
                userGender = "MALE";
            } else userGender = "FEMALE";
        } else userGender = "MALE";

        String userState = "ACTIVE";
        String userAuth = "USER";

        // 해당 아이디가 있는지 확인
        User user = userRepository.findByUserId(userId);

        if(user == null) {
            System.out.println("OAuth 로그인이 최초입니다.");

            user = User.builder()
                    .socialSite(UserSocialSite.valueOf(userSocialSite))
                    .id(userId)
                    .name(userName)
                    .phoneNum(userPhoneNum)
                    .birthDate(userBirthDate)
                    .gender(UserGender.valueOf(userGender))
                    .state(UserState.valueOf(userState))
                    .userAuth(UserAuth.valueOf(userAuth))
                    .build();

            userRepository.save(user);
        } else System.out.println("로그인을 이미 한 적이 있습니다. 당신은 회원 가입이 되어 있습니다.");

        return new PrincipalDetails(user, oAuth2User.getAttributes()); // 생성되면 Authentication 객체 안으로 삽입
    }

}
