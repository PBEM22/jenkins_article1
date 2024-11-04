package article1be.review.controller;

import article1be.review.dto.ReviewDTO;
import article1be.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 모든 리뷰 조회
    @GetMapping
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAllReviews();
    }

    // 특정 사용자의 리뷰 조회
    // 시큐리티 적용 전으로 임의의 userSeq를 사용
    @GetMapping("/user/{userSeq}")
    public List<ReviewDTO> getReviewsByUser(@PathVariable Long userSeq) {
        return reviewService.getReviewsByUser(userSeq);
    }

    // 리뷰 등록
    @PostMapping("/user/{userSeq}")
    public ReviewDTO createReview(@PathVariable Long userSeq, @RequestBody ReviewDTO reviewDto) {
        reviewDto.setUserSeq(userSeq);
        return reviewService.createReview(reviewDto);
    }

    // 리뷰 수정
    @PutMapping("/{reviewSeq}")
    public ReviewDTO updateReview(@PathVariable Long reviewSeq, @RequestBody ReviewDTO reviewDto) {
        return reviewService.updateReview(reviewSeq, reviewDto);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewSeq}")
    public void deleteReview(@PathVariable Long reviewSeq) {
        reviewService.deleteReview(reviewSeq);
    }
}
