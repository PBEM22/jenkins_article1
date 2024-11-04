package article1be.user.controller;

import article1be.config.PrincipalDetails;
import article1be.user.entity.User;
import article1be.user.entity.UserAuth;
import article1be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴
@RequiredArgsConstructor
public class NaverAndGoogleController {

    private final UserRepository userRepository;

    // 스프링 시큐리티는 자기만의 시큐리티 세션을 갖고 있어서 DI를 주입하려면 Authentication 객체가 꼭 필요

    // Authentication 안에 들어갈 수 있는 타입은 두 개
    // UserDetails -> 일반 로그인
    // OAuth2User -> sns 등 OAuth 타입으로 로그인
    // 따라서 컨트롤러에 사용하려면 부모 클래스를 만들어서 상속 시켜 주면 가능

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails userDetails){ // DI(의존성 주입)
        // @AuthenticationPrincipal => 세션 정보에 접근하기
        // PrincipalDetails로도 세션 정보에 접근 가능. UserDetails를 implements 했기 때문
        // 그렇다면 getUsername()으로 호출하는 게 아니라 getUser()로 호출
        System.out.println("/test/login ======================");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal() ;

        System.out.println("authentication = " + principalDetails.getUser()); // authentication.getPrincipal() 리턴 타입 => Object
        System.out.println("userDetails = " + userDetails.getUser());

        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oAuth){ // DI(의존성 주입)
        System.out.println("/test/oauth/login ======================");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal() ;

        System.out.println("authentication = " + oAuth2User.getAttributes()); // authentication.getPrincipal() 리턴 타입 => Object
        System.out.println("oAuth2User = " + oAuth.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }

    // localhost:8080/
    // localhost:8080
    @GetMapping({"","/"})
    public String index(){
        // 머스테치 기본 폴더 src/main/resources/
        // 뷰 리졸버 설정 : templates (prefix), .mustache (suffix) 생략 가능
        return "index";
    }

    // OAuth 로그인을 해도 PrincipalDetails
    // 일반 로그인을 해도 PrincipalDetails
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails = " + principalDetails.getUser());

        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    // 스프링 시큐리티가 해당 주소를 낚아채버려 SecurityConfig 파일 생성 후 작동 X
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println("user = " + user);

        user.setUserAuth(UserAuth.valueOf("USER"));

        userRepository.save(user); // 회원 가입 OK

        return "redirect:/loginForm";
    }

    @Secured("ADMIN") // 하나만 걸고 싶다면 Secured
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인 정보";
    }

    @Secured("ADMIN")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터 정보";
    }

}
