package article1be.user.userInfo;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;
    private Map<String, Object> kakaoAccount; // kakao_account 정보를 저장할 필드

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getGetProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getName() {
        return (String) kakaoAccount.get("name");
    }

    @Override
    public String getGender() {
        return (String) kakaoAccount.get("gender");
    }

    @Override
    public String getBirthday() {
        return (String) kakaoAccount.get("birthday");
    }

    @Override
    public String getBirthyear() {
        return (String) kakaoAccount.get("birthyear");
    }

    @Override
    public String getMobile() {
        return null; // Kakao에서는 phone_number로 제공
    }

    @Override
    public String getPhoneNumber() {
        return (String) kakaoAccount.get("phone_number");
    }

}