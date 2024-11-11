package article1be.user.controller;

import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.security.util.SecurityUtil;
import article1be.user.dto.*;
import article1be.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관련 API")
public class UserController {

    private final UserService userService;

    // 회원 가입 시, 닉네임, 선호도 등록
    @Operation(summary = "닉네임, 선호도 등록", description = "회원 가입 시, 닉네임과 선호도 (스타일, 체질) 등록")
    @PostMapping("/data")
    public ResponseEntity<Void> createUserData(@RequestBody @Valid UserDataDTO userData) {
        userService.createUserData(userData);

        return ResponseEntity.ok().build();
    }
    // 회원 개인 정보 조회
    @Operation(summary = "회원 정보 조회", description = "로그인 한 회원이 자신의 개인 정보를 조회")
    @GetMapping("/detail")
    public ResponseEntity<UserResponseDTO> getUserDetails() {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        UserResponseDTO userDetail = userService.getUserDetail(userSeq);

        System.out.println(userDetail.toString());

        return ResponseEntity.ok(userDetail);
    }
    // 회원 정보 (닉네임) 수정
    @Operation(summary = "닉네임 수정", description = "로그인 한 회원이 자신의 닉네임 수정")
    @PutMapping("/nickname")
    public ResponseEntity<String> updateUserNickname(@RequestBody @Valid UserUpdateDTO updateData) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();

        if(userSeq != null) {
            userService.updateUser(userSeq, updateData.getUserNickname());

            return ResponseEntity.ok("닉네임 수정 성공");
        } else throw new CustomException(ErrorCode.NEED_LOGIN);
    }
    // 회원 탈퇴 (soft delete)
    @Operation(summary = "탈퇴", description = "회원 탈퇴")
    @DeleteMapping
    public ResponseEntity<String> deleteUser() {
        Long userSeq = SecurityUtil.getCurrentUserSeq();

        if(userSeq != null) {
            userService.deleteUser(userSeq);

            return ResponseEntity.ok("탈퇴 성공");
        } else throw new CustomException(ErrorCode.NEED_LOGIN);
    }
    // 로그아웃
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그인 되어 있는 회원이 로그아웃")
    public ResponseEntity<String> logoutUser() {
        Long userSeq = SecurityUtil.getCurrentUserSeq();

        log.info("로그인 되어 있는 userSeq : {}", userSeq);

        if (userSeq != null) {
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok("로그아웃 성공");
        } else throw new CustomException(ErrorCode.NEED_LOGIN);

    }
    // 선호도 조회
    @GetMapping("/preference")
    @Operation(summary = "선호도 조회", description = "로그인 한 회원이 자신의 선호도 (스타일, 체질)를 조회")
    public ResponseEntity<PreferenceResponseDTO> getUserPreference() {
        Long userSeq = SecurityUtil.getCurrentUserSeq();

        PreferenceResponseDTO userPrefer = userService.getUserPreference(userSeq);

        System.out.println(userPrefer.toString());

        return ResponseEntity.ok(userPrefer);
    }
    // 선호도 수정
    @PutMapping("/preference")
    @Operation(summary = "선호도 수정", description = "로그인 한 회원이 자신의 선호도(체질, 스타일)를 수정")
    public ResponseEntity<String> updateUserPreference(@RequestBody @Valid UserPreferDTO preferData) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();

        if(userSeq != null) {
            userService.updatePreference(userSeq, preferData);

            return ResponseEntity.ok("선호도 수정 성공");
        } else throw new CustomException(ErrorCode.NEED_LOGIN);
    }

    @GetMapping("/selectedRecords")
    @Operation(summary = "선택 기록 조회", description = "로그인한 회원이 자신의 모든 선택 기록 조회")
    public ResponseEntity<List<SelectRecordResponseDTO>> getUserSelectedRecords() {
        Long userSeq = SecurityUtil.getCurrentUserSeq(); // 현재 로그인된 회원의 ID 가져오기
        List<SelectRecordResponseDTO> userSelectRecords = userService.getUserSelectRecords(userSeq);
        return ResponseEntity.ok(userSelectRecords);
    }

    @GetMapping("/selectedOutfit")
    @Operation(summary = "선택 복장 리스트 조회", description = "로그인 한 회원이 자신의 선택 기록 번호에 대한 선택 복장 카테고리별 번호, 복장 이름  조회")
    public ResponseEntity<SelectOutfitResponseDTO> getUserSelectedRecord(@RequestParam Long selectSeq) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        SelectOutfitResponseDTO userSelectOutfit = userService.getUserSelectOutfit(userSeq, selectSeq);
        return ResponseEntity.ok(userSelectOutfit);
    }

}
