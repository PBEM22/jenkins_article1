package article1be.user.entity;

import article1be.user.dto.UserDataDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE user SET user_state = 'DELETE', del_date = LOCALTIME WHERE user_seq = ?")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;                       // 회원 번호

    @Column(name = "style_seq")
    private UserStyle styleSeq;                      // 스타일 번호

    @Column(name = "condition_seq")
    private UserCondition conditionSeq;                  // 체질 번호

    @Column(name = "user_social_site")
    @Enumerated(EnumType.STRING)
    private UserSocialSite userSocialSite;      // 소셜 사이트 (KAKAO, NAVER, GOOGLE)

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;                      // 아이디 (계정 이메일)

    @Column(name = "user_name", nullable = false)
    private String userName;                    // 이름

    @Column(name = "user_nickname")
    private String userNickname;                // 닉네임

    @Column(name = "user_phone_num")
    private String userPhoneNum;                // 휴대 전화 번호

    @Column(name = "user_birth_date")
    private LocalDate userBirthDate;            // 생년월일

    @Column(name = "user_gender")
    @Enumerated(EnumType.STRING)
    private UserGender userGender;              // 성별 (MALE, FEMALE)

    @Column(name = "user_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserState userState = UserState.ACTIVE;                // 상태 (ACTIVE, BAN, DELETE)

    @CreatedDate
    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;              // 가입 일자

    @Column(name = "del_date")
    private LocalDateTime delDate;              // 탈퇴 일자

    @Column(name = "user_auth")
    @Enumerated(EnumType.STRING)
    private UserAuth userAuth = UserAuth.USER;                  // 권한 (USER, ADMIN)

    @Builder
    public User(UserSocialSite userSocialSite, String userId, String userName, String userPhoneNum, LocalDate userBirthDate,
                UserGender userGender, UserState userState, UserAuth userAuth) {
        this.userSocialSite = userSocialSite;
        this.userId = userId;
        this.userName = userName;
        this.userPhoneNum = userPhoneNum;
        this.userBirthDate = userBirthDate;
        this.userGender = userGender;
        this.userState = userState;
        this.userAuth = userAuth;
    }

    // 회원 가입 시, 닉네임, 선호도 등록
    public void createUserData(@Valid UserDataDTO userData) {
        this.userNickname = userData.getUserNickname();
        this.styleSeq = userData.getStyleSeq();
        this.conditionSeq = userData.getConditionSeq();
    }

    // 회원 정보 (닉네임) 수정
    public void updateUser(String newNickname) {
        this.userNickname = newNickname;
    }

}
