package article1be.review.service;

import article1be.review.entity.Review;
import article1be.review.dto.ReviewDTO;
import article1be.review.repository.ReviewRepository;
import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 모든 리뷰 조회
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream().map(review -> new ReviewDTO(
                        review.getReviewSeq(),
                        review.getUserSeq(),
                        "userNickname", // 닉네임은 User 테이블과 조인하여 가져와야 함
                        "location",     // 위치 정보 (임의 설정)
                        review.getReviewWeather() + "°C", // 날씨 정보 포맷팅
                        review.getReviewContent(),
                        review.getReviewBlind() ? "BLIND" : "ACTIVE",
                        review.getReviewReport()
                ))
                .collect(Collectors.toList());
    }

    // 특정 사용자의 리뷰 조회
    public List<ReviewDTO> getReviewsByUser(Long userSeq) {
        List<Review> reviews = reviewRepository.findByUserSeq(userSeq);

        return reviews.stream().map(review -> new ReviewDTO(
                        review.getReviewSeq(),
                        review.getUserSeq(),
                        "userNickname", // 닉네임은 User 테이블과 조인하여 가져와야 함
                        "location",     // 위치 정보 (임의 설정)
                        review.getReviewWeather() + "°C",
                        review.getReviewContent(),
                        review.getReviewBlind() ? "BLIND" : "ACTIVE",
                        review.getReviewReport()
                ))
                .collect(Collectors.toList());
    }

    // 리뷰 생성
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDto) {
        Review review = new Review(
                reviewDto.getUserSeq(),
                0L, // selectSeq - 필요시 적절한 값을 설정하거나 DTO에서 받도록 수정 필요
                reviewDto.getReviewContent(),
                Double.parseDouble(reviewDto.getWeather().replace("°C", "")), // 문자열에서 온도 값 추출
                0.0, // location - 적절한 위치 값을 설정 필요
                reviewDto.getReviewStatus().equals("BLIND"),
                false,
                Optional.ofNullable(reviewDto.getReviewReport()).orElse(0)
        );

        review = reviewRepository.save(review);

        return new ReviewDTO(
                review.getReviewSeq(),
                review.getUserSeq(),
                "userNickname",
                "location",
                review.getReviewWeather() + "°C",
                review.getReviewContent(),
                review.getReviewBlind() ? "BLIND" : "ACTIVE",
                review.getReviewReport()
        );
    }

    // 리뷰 수정
    @Transactional
    public ReviewDTO updateReview(Long reviewSeq, ReviewDTO reviewDto) {
        Review review = reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        review.updateReview(
                reviewDto.getReviewContent(),
                Double.parseDouble(reviewDto.getWeather().replace("°C", "")),
                0.0, // location - 적절한 위치 값을 설정 필요
                reviewDto.getReviewStatus().equals("BLIND"),
                false
        );

        reviewRepository.save(review);

        return new ReviewDTO(
                review.getReviewSeq(),
                review.getUserSeq(),
                "userNickname",
                "location",
                review.getReviewWeather() + "°C",
                review.getReviewContent(),
                review.getReviewBlind() ? "BLIND" : "ACTIVE",
                review.getReviewReport()
        );
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewSeq) {
        Review review = reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        reviewRepository.delete(review);
    }

    // 리뷰 신고 추가
    @Transactional
    public ReviewDTO addReportToReview(Long reviewSeq) {
        Review review = reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        review.addReport();  // 신고 횟수 증가 및 블라인드 처리

        reviewRepository.save(review);

        return new ReviewDTO(
                review.getReviewSeq(),
                review.getUserSeq(),
                "userNickname",
                "location",
                review.getReviewWeather() + "°C",
                review.getReviewContent(),
                review.getReviewBlind() ? "BLIND" : "ACTIVE",
                review.getReviewReport()
        );
    }
}
