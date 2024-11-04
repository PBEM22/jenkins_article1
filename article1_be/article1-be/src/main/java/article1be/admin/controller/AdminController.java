package article1be.admin.controller;

import article1be.admin.dto.AdminDTO;
import article1be.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "관리자 회원 조회" , description = "관리자의 회원 조회 및 상태 변경")
public class AdminController {

    private final AdminService adminService;

    // 전체 회원 정보 조회
    @GetMapping("/user")
    @Operation(summary = "관리자의 전체 회원 조회" , description = "관리자의 전체 회원 조회")
    public List<AdminDTO.UserInfo> getAllUsers() {
        return adminService.getAllMembers();
    }

    // 회원 정보 상세 조회
    @PostMapping("/user/")
    @Operation(summary = "관리자의 상세 회원 조회" , description = "관리자의 상세 회원 조회")
    public AdminDTO.UserInfo getUserDetail(@RequestBody AdminDTO.UserStatusUpdateRequest request) {
        return adminService.getMemberDetail(request.getUserSeq());
    }

    // 회원 상태 변경
    @PutMapping("/user/status")
    @Operation(summary = "관리자의 회원 상태 변경" , description = "ACTIVE, BAN , DELETE")
    public void updateMemberStatus(@RequestBody AdminDTO.UserStatusUpdateRequest statusUpdate) {
        adminService.updateUserStatus(statusUpdate);
    }

}
