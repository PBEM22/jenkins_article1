package article1be.admin.service;

import article1be.admin.dto.AdminDTO;
import article1be.admin.repository.AdminRepository;
import article1be.user.entity.User;
import article1be.user.entity.UserAuth;
import article1be.user.entity.UserGender;
import article1be.user.entity.UserSocialSite;
import article1be.user.entity.UserState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    @DisplayName("전체 회원 정보 조회 테스트")
    @Test
    void testGetAllMembers() {
        // given
        User user = new User(
                UserSocialSite.KAKAO,
                "test@kakao.com",
                "hyun",
                "010-1234-5678",
                LocalDate.of(1990, 1, 1),
                UserGender.MALE,
                UserState.ACTIVE,
                UserAuth.USER
        );
        user.updateUser("hhyun");

        when(adminRepository.findAll()).thenReturn(List.of(user));

        // when
        List<AdminDTO.UserInfo> result = adminService.getAllMembers();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@kakao.com", result.get(0).getUserId());
        assertEquals("hhyun", result.get(0).getUserNickname());
    }

    @DisplayName("회원 정보 상세 조회 테스트")
    @Test
    void testGetMemberDetail() {
        // given
        User user = new User(
                UserSocialSite.KAKAO,
                "test@kakao.com",
                "hyun",
                "010-1234-5678",
                LocalDate.of(1990, 1, 1),
                UserGender.MALE,
                UserState.ACTIVE,
                UserAuth.USER
        );
        user.updateUser("hhyun");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        AdminDTO.UserInfo result = adminService.getMemberDetail(1L);

        // then
        assertNotNull(result);
        assertEquals("test@kakao.com", result.getUserId());
        assertEquals("hhyun", result.getUserNickname());
    }
}
