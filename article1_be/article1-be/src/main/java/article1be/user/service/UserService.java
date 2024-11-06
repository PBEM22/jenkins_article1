package article1be.user.service;

import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.security.util.SecurityUtil;
import article1be.user.dto.UserDataDTO;
import article1be.user.dto.UserResponseDTO;
import article1be.user.entity.User;
import article1be.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userSeq) throws UsernameNotFoundException {
        /* 인증 토큰에 담긴 userSeq가 메소드로 넘어오므로 해당 값을 기준으로 DB에서 조회한다. (전달된 아이디 기준으로 DB 조회한다) */

        User loginUser = userRepository.findByUserSeq(Long.parseLong(userSeq))  // JPA 쓰고 있으므로 findByUserId 활용
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userSeq: " + userSeq));  // Id가 존재하지 않으면(해당 Id가 조회되지 않으면) Exception 발생

        /* 권한이 여러가지 있을 수 있다. 기본적으로 권한은 Collection Type으로 담도록 되어있다.
         *  우리는 심플하게 두가지(USER, ADMIN)로만 나누었기 때문에 코드로 가공해서 기재되어 있는 USER or ADMIN 권한 하나만 넣도록 한다. */
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(loginUser.getUserAuth().name()));

        // 소셜 로그인에서는 비밀번호를 사용하지 않으므로 빈 문자열로 처리
        String password = "";

        // Spring Security에서 제공하는 User 객체 반환
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(loginUser.getUserSeq()), password, grantedAuthorities);
    }


    /* 회원 닉네임, 선호도 등록 */
    @Transactional
    public void createUserData(@Valid UserDataDTO userData) {

        // 닉네임 중복 검증
        if (checkUserNickname(userData.getUserNickname(), null)) {

            Long userSeq = SecurityUtil.getCurrentUserSeq();

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
