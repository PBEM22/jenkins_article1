package article1be.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

public class AdminDTO {

    @Data
    @AllArgsConstructor
    public static class MemberInfo {
        private Long userSeq;
        private String userId;
        private String userName;
        private String userNickname;
        private String userPhoneNum;
        private LocalDate userBirthDate;
        private String userGender;
        private String userState;
    }

    @Data
    @AllArgsConstructor
    public static class MemberStatusUpdateRequest {
        private Long userSeq;
        private String userState;
    }
}
