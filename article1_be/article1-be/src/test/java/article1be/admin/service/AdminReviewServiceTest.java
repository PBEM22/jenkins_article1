package article1be.admin.service;

import article1be.admin.dto.AdminReviewDTO;
import article1be.admin.repository.AdminReviewRepository;
import article1be.outfit.entity.Outfit;
import article1be.outfit.entity.SelectOutfit;
import article1be.outfit.entity.SelectRecord;
import article1be.outfit.repository.SelectOutfitRepository;
import article1be.review.entity.Review;
import article1be.user.entity.*;
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

    @Mock
    private SelectOutfitRepository selectOutfitRepository;

    @Mock
    private SelectRecord selectRecord;

    @Mock
    private Outfit outfit; // Outfit을 Mock으로 설정

    @InjectMocks
    private AdminReviewService adminReviewService;

    @DisplayName("전체 리뷰 목록 조회 테스트")
    @Test
    void testGetAllReviews() {
        // given
        Review review = new Review(1L, 1L, "xxx", 22.5, "신대방", false, false);
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

        // Mock된 Outfit 설정
        when(outfit.getOutfitName()).thenReturn("Casual Outfit");

        // SelectOutfit 설정
        SelectOutfit selectOutfit = SelectOutfit.create(selectRecord, outfit);
        List<SelectOutfit> selectOutfits = List.of(selectOutfit);

        when(reviewRepository.findAll()).thenReturn(List.of(review));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(selectOutfitRepository.findBySelectRecord_SelectSeqWithOutfit(review.getSelectSeq()))
                .thenReturn(selectOutfits);

        // when
        List<AdminReviewDTO.ReviewInfo> result = adminReviewService.getAllReviews();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("xxx", result.get(0).getReviewContent());
        assertEquals("hhyun", result.get(0).getUserNickname());
        assertEquals(1, result.get(0).getOutfits().size());
        assertEquals("Casual Outfit", result.get(0).getOutfits().get(0).getOutfitName());
    }

    @DisplayName("리뷰 블라인드 상태 업데이트 테스트")
    @Test
    void testUpdateReviewStatus() {
        // given
        Review review = new Review(1L, 1L, "xxxx", 22.5, "신대방", false, false);
        AdminReviewDTO.ReviewStatusUpdateRequest request = new AdminReviewDTO.ReviewStatusUpdateRequest(1L, true);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // when
        adminReviewService.updateReviewStatus(request);

        // then
        assertTrue(review.getReviewBlind());
        verify(reviewRepository, times(1)).save(review);
    }
}
