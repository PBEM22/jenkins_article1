package article1be.admin.controller;

import article1be.admin.dto.AdminDTO;
import article1be.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 전체 회원 정보 조회
    @GetMapping("/members")
    public List<AdminDTO.MemberInfo> getAllMembers() {
        return adminService.getAllMembers();
    }

    // 회원 정보 상세 조회
    @PostMapping("/members/")
    public AdminDTO.MemberInfo getMemberDetail(@RequestBody AdminDTO.MemberStatusUpdateRequest request) {
        return adminService.getMemberDetail(request.getUserSeq());
    }

    // 회원 상태 변경
    @DeleteMapping("/members/")
    public void updateMemberStatus(@RequestBody AdminDTO.MemberStatusUpdateRequest statusUpdate) {
        adminService.updateMemberStatus(statusUpdate);
    }


    // 전체 리뷰 목록 조회
    @GetMapping("/reviews")
    public List<AdminDTO.ReviewInfo> getAllReviews() {
        return adminService.getAllReviews();
    }

    // 특정 리뷰 정보 조회
    @PostMapping("/reviewsl")
    public AdminDTO.ReviewInfo getReviewDetail(@RequestBody AdminDTO.ReviewBlindStatusUpdateRequest request) {
        return adminService.getReviewDetail(request.getReviewSeq());
    }

    // 리뷰 블라인드 상태 업데이트
    @PutMapping("/reviewsd")
    public void updateReviewBlindStatus(@RequestBody AdminDTO.ReviewBlindStatusUpdateRequest statusUpdate) {
        adminService.updateReviewBlindStatus(statusUpdate);
    }
}
