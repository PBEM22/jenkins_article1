package article1be.user.dto;

import lombok.Data;

@Data
public class KakaoAccount {
    private String email;           // 카카오계정 대표 이메일
    private String name;            // 카카오계정 이름
    private String phone_number;    // 카카오계정 전화번호
    private String birthyear;       // 출생 연도(YYYY)
    private String birthday;        // 생일(MMDD)
    private String gender;          // 성별(female:여성, male:남성)
}
