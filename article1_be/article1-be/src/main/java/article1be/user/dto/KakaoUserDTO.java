package article1be.user.dto;

import lombok.Data;

@Data
public class KakaoUserDTO {
    private Long id;                        // 회원번호
    private KakaoAccount kakao_account;     // 사용자계정 정보
}
