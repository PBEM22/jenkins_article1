package article1be.review.controller;

import article1be.review.dto.ReviewDTO;
import article1be.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/review/{reviewseq}")    // 리뷰 조회
    public ReviewDTO getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping("/review/{userSeq}")  // 특정 유저의 모든 리뷰 조회
    public List<ReviewDTO> getReviewsByUser(@PathVariable Long userSeq) {
        return reviewService.getReviewsByUser(userSeq);
    }
    @PostMapping    // 리뷰 생성
    public ReviewDTO createReview(@RequestBody ReviewDTO reviewDto) {
        return reviewService.createReview(reviewDto);
    }
    
    @PutMapping("/review/{reviewseq}")    // 리뷰 업데이트
    public ReviewDTO updateReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDto) {
        return reviewService.updateReview(id, reviewDto);
    }

    @DeleteMapping("/review/{reviewseq}")  // 리뷰 삭제
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
