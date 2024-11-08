package article1be.admin.controller;

import article1be.admin.dto.AdminReviewDTO;
import article1be.admin.service.AdminReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/review")
@RequiredArgsConstructor
@Tag(name = "관리자 관련 API", description = "관리자 권한 사용자가 사용하는 API")
public class AdminReviewController {

    private final AdminReviewService reviewService;

    // 전체 리뷰 목록 조회
    @GetMapping
    @Operation(summary = "전체 리뷰 조회", description = "관리자가 모든 리뷰를 조회할 수 있습니다.")
    public List<AdminReviewDTO.ReviewInfo> getAllReviews() {
        return reviewService.getAllReviews();
    }

    // 리뷰 블라인드 상태 업데이트
    @PutMapping("/status")
    @Operation(summary = "리뷰 블라인드 상태 업데이트", description = "관리자가 리뷰의 블라인드 상태를 업데이트할 수 있습니다.")
    public void updateReviewStatus(@RequestBody AdminReviewDTO.ReviewStatusUpdateRequest request) {
        reviewService.updateReviewStatus(request);
    }
}
