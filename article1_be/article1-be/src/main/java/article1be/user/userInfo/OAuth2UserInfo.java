package article1be.user.userInfo;

public interface OAuth2UserInfo {

    String getProviderId(); // google, naver, kakao
    String getGetProvider(); // google, naver, kakao
    String getEmail(); // google, naver, kakao
    String getName(); // google, naver, kakao
    String getGender(); // naver, kakao
    String getBirthday(); // naver, kakao
    String getBirthyear(); // naver, kakao
    String getMobile(); // naver
    String getPhoneNumber(); // kakao

}