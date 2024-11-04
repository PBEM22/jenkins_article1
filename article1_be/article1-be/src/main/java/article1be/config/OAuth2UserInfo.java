package article1be.config;

public interface OAuth2UserInfo {
    String getProviderId(); // google, naver
    String getGetProvider(); // google, naver, kakao
    String getName(); // google, naver, kakao
    String getEmail(); // kakao
    String getMobile(); // naver
    String getPhoneNumber(); // kakao
    String getBirthyear(); // naver, kakao
    String getBirthday(); // naver, kakao
    String getGender(); // naver, kakao
}
