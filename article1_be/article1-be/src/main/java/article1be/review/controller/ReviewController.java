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

    @GetMapping("/review/{userSeq}")  // 자신의 모든 리뷰 조회
    public List<ReviewDTO> getReviewsByUser(@PathVariable Long userSeq) {
        return reviewService.getReviewsByUser(userSeq);
    }

    @PostMapping()    // 리뷰 작성
    public ReviewDTO createReview(@PathVariable Long userSeq, @RequestBody ReviewDTO reviewDto) {
        reviewDto.setSelectSeq(userSeq);
        return reviewService.createReview(reviewDto);
    }

    @PutMapping("/{reviewSeq}")    // 리뷰 수정
    public ReviewDTO updateReview(@PathVariable Long reviewSeq, @RequestBody ReviewDTO reviewDto) {
        return reviewService.updateReview(reviewSeq, reviewDto);
    }

    @DeleteMapping("/{reviewSeq}")  // 리뷰 삭제
    public void deleteReview(@RequestBody ReviewDTO reviewDto) {
        reviewService.deleteReview(reviewDto.getReviewSeq());
    }

}
