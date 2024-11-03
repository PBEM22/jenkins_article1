package article1be.user.service;

import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.user.dto.UserDataDTO;
import article1be.user.dto.UserResponseDTO;
import article1be.user.entity.User;
import article1be.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* 회원 닉네임, 선호도 등록 */
    @Transactional
    public void createUserData(@Valid UserDataDTO userData) {

        // 닉네임 중복 검증
        if (checkUserNickname(userData.getUserNickname(), null)) {

            // 시큐리티 완성 후 적용
            //Long userSeq = SecurityUtil.getCurrentUserSeq();
            Long userSeq = 21L;  // 시큐리티 완성 전 테스트용 하드코딩

            User findUser = userRepository.findByUserSeq(userSeq)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

            findUser.createUserData(userData);
        }

    }

    /* 회원정보(닉네임) 수정 */
    @Transactional
    public void updateUser(Long userSeq, String newNickname) {

        // 닉네임 중복 검증
        if (checkUserNickname(newNickname, userSeq)) {
            log.info("회원번호 : {}", userSeq);
            log.info("새로운 닉네임 : {}", newNickname);

            User findUser = userRepository.findByUserSeq(userSeq)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

            findUser.updateUser(newNickname);
            userRepository.save(findUser);
        }
    }

    /* 회원 탈퇴 (soft delete) */
    @Transactional
    public void deleteUser(Long userSeq) {

        userRepository.deleteById(userSeq);
    }

    /* 회원 개인정보 조회 */
    @Transactional
    public UserResponseDTO getUserDetail(Long userSeq) {

        User findUser = userRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));


        return new UserResponseDTO(
                findUser.getUserSeq(),
                findUser.getUserSocialSite(),
                findUser.getUserId(),
                findUser.getUserName(),
                findUser.getUserNickname(),
                findUser.getUserPhoneNum(),
                findUser.getUserBirthDate(),
                findUser.getUserGender(),
                findUser.getRegDate()
        );

    }

    /* 닉네임 중복 검증 */
    @Transactional
    public boolean checkUserNickname(String userNickname, Long userSeq) {
        Optional<User> findNickname = userRepository.findByUserNickname(userNickname);

        if (findNickname.isEmpty()) {
            return true;
        }

        if (Objects.equals(findNickname.get().getUserSeq(), userSeq)) {
            return true;
        }

        log.info("닉네임 값 중복 {}", userNickname);
        throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);

    }


}
