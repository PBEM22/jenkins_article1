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

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(review -> new ReviewDTO(
                        review.getReviewSeq(),
                        review.getUserSeq(),
                        "userNickname",
                        "location",
                        review.getReviewWeather() + "°C",
                        review.getReviewContent(),
                        review.getReviewBlind() ? "BLIND" : "ACTIVE",
                        review.getReviewReport()
                ))
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByUser(Long userSeq) {
        return reviewRepository.findByUserSeq(userSeq).stream()
                .map(review -> new ReviewDTO(
                        review.getReviewSeq(),
                        review.getUserSeq(),
                        "userNickname",
                        "location",
                        review.getReviewWeather() + "°C",
                        review.getReviewContent(),
                        review.getReviewBlind() ? "BLIND" : "ACTIVE",
                        review.getReviewReport()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDto) {
        Review review = new Review(
                reviewDto.getUserSeq(),
                Optional.ofNullable(reviewDto.getReviewSeq()).orElse(0L),
                reviewDto.getReviewContent(),
                parseWeather(reviewDto.getWeather()),
                Optional.ofNullable(reviewDto.getLocation()).map(Double::parseDouble).orElse(0.0),
                "BLIND".equals(reviewDto.getReviewStatus()),
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

    @Transactional
    public ReviewDTO updateReview(Long reviewSeq, ReviewDTO reviewDto) {
        Review review = reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        review.updateReview(
                reviewDto.getReviewContent(),
                parseWeather(reviewDto.getWeather()),
                Optional.ofNullable(reviewDto.getLocation()).map(Double::parseDouble).orElse(0.0),
                "BLIND".equals(reviewDto.getReviewStatus()),
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

    @Transactional
    public void deleteReview(Long reviewSeq) {
        Review review = reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));
        reviewRepository.delete(review);
    }

    @Transactional
    public ReviewDTO addReportToReview(Long reviewSeq) {
        Review review = reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));
        review.addReport();
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

    private Double parseWeather(String weather) {
        return Double.parseDouble(weather.replace("°C", ""));
    }
}
