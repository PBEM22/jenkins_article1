package article1be.config;

public interface OAuth2UserInfo {

    String getProviderId(); // google, naver
    String getGetProvider(); // google, naver
    String getName(); // google, naver
    String getMobile(); // naver
    String getBirthyear(); // naver
    String getBirthday(); // naver
    String getGender(); // naver

}
