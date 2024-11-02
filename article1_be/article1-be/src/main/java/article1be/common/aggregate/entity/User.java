package article1be.common.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "USER")
@Getter
@SQLDelete(sql = "UPDATE user SET user_state = 'DELETE', del_date = LOCALTIME WHERE user_seq = ?")
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;                       // 회원번호

    @Column(name = "user_social_site")
    @Enumerated(EnumType.STRING)
    private UserSocialSite userSocialSite;      // 소셜사이트 (KAKAO, NAVER, GOOGLE)

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;                      // 아이디(계정 이메일)

    @Column(name = "user_name", nullable = false)
    private String userName;                    // 이름

    @Column(name = "user_nickname")
    private String userNickname;                // 닉네임

    @Column(name = "user_phone_num", unique = true)
    private String userPhoneNum;                // 휴대폰 번호

    @Column(name = "user_birth_date")
    private LocalDate userBirthDate;            // 생년월일

    @Column(name = "user_gender")
    @Enumerated(EnumType.STRING)
    private UserGender userGender;              // 성별 Enum (MALE, FEMALE)

    @Column(name = "user_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserState userState;                // 상태 Enum (ACTIVE, BAN, DELETE)

    @Column(name = "del_date")
    private LocalDateTime delDate;              // 탈퇴일자

    @Column(name = "user_auth")
    @Enumerated(EnumType.STRING)
    private UserAuth userAuth;            // 권한 Enum (USER, ADMIN)


    @Builder
    public User(UserSocialSite socialSite, String id, String name, String phoneNum, LocalDate birthDate,
                UserGender gender, UserState state, UserAuth userAuth) {
        this.userSocialSite = socialSite;
        this.userId = id;
        this.userName = name;
        this.userPhoneNum = phoneNum;
        this.userBirthDate = birthDate;
        this.userGender = gender;
        this.userState = state;
        this.userAuth = userAuth;
    }

}
