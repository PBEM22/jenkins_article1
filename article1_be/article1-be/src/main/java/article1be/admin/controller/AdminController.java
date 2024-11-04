package article1be.admin.controller;

import article1be.admin.dto.AdminDTO;
import article1be.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
@Tag(name = "관리자 회원 조회" , description = "관리자의 회원 조회 및 상태 변경")
public class AdminController {

    private final AdminService adminService;

    // 전체 회원 정보 조회
    @GetMapping("/user")
    @Operation(summary = "관리자의 전체 회원 조회", description = "관리자의 전체 회원 조회")
    public List<AdminDTO.UserInfo> getAllUsers() {
        return adminService.getAllMembers();
    }

    // 회원 정보 상세 조회
    @GetMapping("/user/{userSeq}")
    @Operation(summary = "관리자의 상세 회원 조회", description = "관리자의 상세 회원 조회")
    public AdminDTO.UserInfo getUserDetail(@PathVariable Long userSeq) {
        return adminService.getMemberDetail(userSeq);
    }

    // 회원 상태 변경
    @PutMapping("/user/update")
    @Operation(summary = "관리자의 회원 상태 및 닉네임 변경", description = "회원의 상태와 닉네임을 동시에 변경할 수 있습니다.")
    public void updateMember(@RequestBody AdminDTO.UserUpdateRequest updateRequest) {
        adminService.updateUser(updateRequest);
    }
}
