package article1be.config;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes; // => oAuth2User.getAttributes()

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes =attributes;
    }

    @Override
    public String getProviderId() {
        return (String)attributes.get("id");
    }

    @Override
    public String getGetProvider() {
        return "naver";
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getMobile() {
        return (String)attributes.get("mobile");
    }

    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public String getBirthyear() {
        return (String)attributes.get("birthyear");
    }

    @Override
    public String getBirthday() {
        return (String)attributes.get("birthday");
    }

    @Override
    public String getGender() {
        return (String)attributes.get("gender");
    }

}
