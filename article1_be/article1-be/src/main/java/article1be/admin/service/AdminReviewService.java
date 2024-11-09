package article1be.admin.service;

import article1be.admin.dto.AdminReviewDTO;
import article1be.admin.repository.AdminReviewRepository;
import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.outfit.dto.OutfitResponseDTO;
import article1be.outfit.repository.SelectOutfitRepository;
import article1be.review.entity.Review;
import article1be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final AdminReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final SelectOutfitRepository selectOutfitRepository;

    // 전체 리뷰 목록 조회
    public List<AdminReviewDTO.ReviewInfo> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(review -> {
                    String userNickname = userRepository.findById(review.getUserSeq())
                            .map(user -> user.getUserNickname())
                            .orElse("Unknown User");

                    List<OutfitResponseDTO> outfits = selectOutfitRepository.findBySelectRecord_SelectSeqWithOutfit(review.getSelectSeq())
                            .stream()
                            .map(selectOutfit -> new OutfitResponseDTO(
                                    selectOutfit.getOutfit().getOutfitSeq(),
                                    selectOutfit.getOutfit().getOutfitName()
                            ))
                            .collect(Collectors.toList());

                    return new AdminReviewDTO.ReviewInfo(
                            review.getReviewSeq(),
                            review.getUserSeq(),
                            userNickname,
                            review.getReviewLocation(),
                            review.getReviewWeather(),
                            review.getReviewContent(),
                            review.getReviewLikeYn(),
                            review.getReviewBlind(),
                            review.getRegDate(),
                            outfits // 의상 정보 포함
                    );
                })
                .collect(Collectors.toList());
    }

    // 리뷰 블라인드 상태 업데이트
    public void updateReviewStatus(AdminReviewDTO.ReviewStatusUpdateRequest request) {
        Review review = reviewRepository.findById(request.getReviewSeq())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        // 블라인드 상태 업데이트
        review.updateReview(
                review.getReviewContent(),
                review.getReviewWeather(),
                review.getReviewLocation(),
                request.getReviewBlind(),
                review.getReviewLikeYn()
        );

        reviewRepository.save(review);
    }
}
