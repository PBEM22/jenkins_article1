package article1be.admin.service;

import article1be.admin.dto.AdminReviewDTO;
import article1be.admin.repository.AdminReviewRepository;
import article1be.review.entity.Review;
import article1be.user.entity.User;
import article1be.user.entity.UserAuth;
import article1be.user.entity.UserGender;
import article1be.user.entity.UserSocialSite;
import article1be.user.entity.UserState;
import article1be.user.repository.UserRepository;
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
class AdminReviewServiceTest {

    @Mock
    private AdminReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminReviewService adminReviewService;

    @DisplayName("전체 리뷰 목록 조회 테스트")
    @Test
    void testGetAllReviews() {
        // given
        Review review = new Review(1L, 1L, "xxx", 22.5, 126.8, false, false);
        User user = new User(
                UserSocialSite.KAKAO,
                "test@ekakao.com",
                "hyun",
                "010-1234-5678",
                LocalDate.of(1995, 1, 1),
                UserGender.MALE,
                UserState.ACTIVE,
                UserAuth.USER
        );
        user.updateUser("hhyun");

        when(reviewRepository.findAll()).thenReturn(List.of(review));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        List<AdminReviewDTO.ReviewInfo> result = adminReviewService.getAllReviews();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("xxx", result.get(0).getReviewContent());
        assertEquals("hhyun", result.get(0).getUserNickname());
    }

    @DisplayName("리뷰 블라인드 상태 업데이트 테스트")
    @Test
    void testUpdateReviewStatus() {
        // given
        Review review = new Review(1L, 1L, "xxxx", 22.5, 126.8, false, false);
        AdminReviewDTO.ReviewStatusUpdateRequest request = new AdminReviewDTO.ReviewStatusUpdateRequest(1L, true);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // when
        adminReviewService.updateReviewStatus(request);

        // then
        assertTrue(review.getReviewBlind());
        verify(reviewRepository, times(1)).save(review);
    }
}
