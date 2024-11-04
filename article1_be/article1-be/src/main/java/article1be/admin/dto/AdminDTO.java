package article1be.admin.dto;

import article1be.user.entity.UserGender;
import article1be.user.entity.UserState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


public class AdminDTO {

    @Data
    @AllArgsConstructor
    public static class UserInfo {
        private Long userSeq;
        private String userId;
        private String userName;
        private String userNickname;
        private String userPhoneNum;
        private LocalDate userBirthDate;
        private UserGender userGender;
        private UserState userState;
    }

    @Data
    @AllArgsConstructor
    public static class UserStatusUpdateRequest {
        private Long userSeq;
        private UserState userState;
    }

}
