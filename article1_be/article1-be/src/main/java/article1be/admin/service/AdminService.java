package article1be.admin.service;

import article1be.admin.dto.AdminDTO;
import article1be.admin.repository.AdminRepository;
import article1be.common.aggregate.entity.User;
import article1be.common.aggregate.entity.UserState;
import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.review.aggregate.Review;
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
        return adminRepository.findAll().stream()
                .map(user -> new AdminDTO.MemberInfo(
                        user.getUserSeq(),
                        user.getUserId(),
                        user.getUserName(),
                        user.getUserNickname(),
                        user.getUserPhoneNum(),
                        user.getUserBirthDate(),
                        user.getUserGender().name(),
                        user.getUserState().name()
                )).collect(Collectors.toList());
    }

    // 특정 회원 상세 정보 조회
    public AdminDTO.MemberInfo getMemberDetail(Long userSeq) {
        User user = adminRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return new AdminDTO.MemberInfo(
                user.getUserSeq(),
                user.getUserId(),
                user.getUserName(),
                user.getUserNickname(),
                user.getUserPhoneNum(),
                user.getUserBirthDate(),
                user.getUserGender().name(),
                user.getUserState().name()
        );
    }

    // 회원 상태 변경
    public void updateMemberStatus(AdminDTO.MemberStatusUpdateRequest statusUpdate) {
        User user = adminRepository.findById(statusUpdate.getUserSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        UserState newState = parseUserState(statusUpdate.getUserState());
        user.changeUserState(newState);

        adminRepository.save(user);
    }

    private UserState parseUserState(String state) {
        try {
            return UserState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
    }
    public List<AdminDTO.ReviewInfo> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(review -> new AdminDTO.ReviewInfo(
                        review.getReviewSeq(),
                        review.getUserSeq(),
                        review.getReviewContent(),
                        review.getReviewBlind(),
                        review.getRegDate().toLocalDate()
                )).collect(Collectors.toList());
    }

    // ADM 005: 특정 리뷰 정보 조회
    public AdminDTO.ReviewInfo getReviewDetail(Long reviewSeq) {
        Review review = reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return new AdminDTO.ReviewInfo(
                review.getReviewSeq(),
                review.getUserSeq(),
                review.getReviewContent(),
                review.getReviewBlind(),
                review.getRegDate().toLocalDate()
        );
    }

    // ADM 006: 리뷰 블라인드 상태 업데이트
    public void updateReviewBlindStatus(AdminDTO.ReviewBlindStatusUpdateRequest statusUpdate) {
        Review review = reviewRepository.findById(statusUpdate.getReviewSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        review.updateReview(
                review.getReviewContent(),
                review.getReviewWeather(),
                review.getReviewLocation(),
                statusUpdate.getReviewBlind(),
                review.getReviewLikeYn()
        );

        reviewRepository.save(review);
    }
}
