package article1be.admin.service;

import article1be.admin.dto.AdminDTO;
import article1be.admin.repository.AdminRepository;
import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.user.entity.User;
import article1be.user.entity.UserState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    // 전체 회원 정보 조회
    public List<AdminDTO.MemberInfo> getAllMembers() {
        List<User> users = adminRepository.findAll();
        if (users.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER); // 사용자가 없을 경우 예외 발생
        }
        return users.stream()
                .map(user -> new AdminDTO.MemberInfo(
                        user.getUserSeq(),
                        user.getUserId(),
                        user.getUserName(),
                        user.getUserNickname(),
                        user.getUserPhoneNum(),
                        user.getUserBirthDate(),
                        user.getUserGender(),
                        user.getUserState()))
                .collect(Collectors.toList());
    }

    // 회원 정보 상세 조회
    public AdminDTO.MemberInfo getMemberDetail(Long userSeq) {
        User user = adminRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)); // 사용자를 찾을 수 없을 경우 예외 발생
        return new AdminDTO.MemberInfo(
                user.getUserSeq(),
                user.getUserId(),
                user.getUserName(),
                user.getUserNickname(),
                user.getUserPhoneNum(),
                user.getUserBirthDate(),
                user.getUserGender(),
                user.getUserState()
        );
    }

    // 회원 상태 변경
    public void updateMemberStatus(AdminDTO.MemberStatusUpdateRequest statusUpdate) {
        User user = adminRepository.findById(statusUpdate.getUserSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        user.setUserState(statusUpdate.getUserState()); // 전달받은 상태로 업데이트
        adminRepository.save(user);
    }
}
