package article1be.user.service;

import article1be.user.dto.UserResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @DisplayName("회원 번호 별 회원 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2"})
    void testGetUserDetail(long userSeq) {
        Assertions.assertDoesNotThrow(
                () -> {
                    UserResponseDTO findUser = UserService.getUserDetail(userSeq);
                    System.out.println(findUser);
                }
        );
    }

}