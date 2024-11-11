package article1be.review.service;

import article1be.outfit.repository.SelectOutfitRepository;
import article1be.outfit.repository.SelectRecordRepository;
import article1be.review.dto.ReviewDTO;
import article1be.review.entity.Review;
import article1be.review.repository.ReviewRepository;
import article1be.user.entity.*;
import article1be.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SelectRecordRepository selectRecordRepository;

    @Mock
    private SelectOutfitRepository selectOutfitRepository;

    @InjectMocks
    private ReviewService reviewService;

    @DisplayName("전체 리뷰 조회 테스트")
    @Test
    void testGetAllReviews() {
        // given
        Review review = new Review(1L, 1L, "xxxx", 22.5, "신대방", false, false);
        User user = new User(UserSocialSite.KAKAO, "test@kakao.com", "hyun", "010-1234-5678",
                LocalDate.of(1990, 1, 1), UserGender.MALE, UserState.ACTIVE, UserAuth.USER);

        when(reviewRepository.findAll()).thenReturn(List.of(review));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        List<ReviewDTO> result = reviewService.getAllReviews();

        // then
        assertNotNull(result);
        result.forEach(System.out::println);
    }

    @DisplayName("리뷰 생성 테스트")
    @Test
    void testCreateReview() {
        // given
        ReviewDTO reviewDto = new ReviewDTO(1L, 1L, 1L, "hyun", "신대방", 22.5, "xxxx", "ACTIVE", false, false, LocalDateTime.of(2023, 1, 1, 0, 0), List.of());

        Review savedReview = new Review(1L, 1L, "xxxxx", 22.5, "신대방", false, false);
        User user = new User(UserSocialSite.KAKAO, "test@kakao.com", "hyun", "010-1234-5678",
                LocalDate.of(1990, 1, 1), UserGender.MALE, UserState.ACTIVE, UserAuth.USER);

        when(selectRecordRepository.existsById(1L)).thenReturn(true);
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        ReviewDTO createdReview = reviewService.createReview(reviewDto);

        // then
        assertNotNull(createdReview);
    }

    @DisplayName("리뷰 업데이트 테스트")
    @Test
    void testUpdateReview() {
        // given
        Review review = new Review(1L, 1L, "xxxxx", 22.5, "신대방", true, true);
        ReviewDTO reviewDto = new ReviewDTO(1L, 1L, 1L, "hyun", "신대방", 22.5, "xxxx", "ACTIVE", false, false, LocalDateTime.of(2023, 1, 1, 0, 0), List.of());

        User user = new User(UserSocialSite.KAKAO, "test@kakao.com", "hyun", "010-1234-5678",
                LocalDate.of(1990, 1, 1), UserGender.MALE, UserState.ACTIVE, UserAuth.USER);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        ReviewDTO updatedReview = reviewService.updateReview(1L, reviewDto);

        // then
        assertNotNull(updatedReview);
    }
}
