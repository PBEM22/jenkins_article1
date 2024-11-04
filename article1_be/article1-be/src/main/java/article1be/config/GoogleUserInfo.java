package article1be.config;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes; // => oAuth2User.getAttributes()

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes =attributes;
    }

    @Override
    public String getProviderId() {
        return (String)attributes.get("email");
    }

    @Override
    public String getGetProvider() {
        return "google";
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    @Override
    public String getMobile() {
        return null;
    }

    @Override
    public String getBirthyear() {
        return null;
    }

    @Override
    public String getBirthday() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

}
