package article1be.review.service;

import article1be.outfit.dto.OutfitResponseDTO;
import article1be.outfit.repository.SelectOutfitRepository;
import article1be.outfit.repository.SelectRecordRepository;
import article1be.review.entity.Review;
import article1be.review.dto.ReviewDTO;
import article1be.review.repository.ReviewRepository;
import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.security.util.SecurityUtil;
import article1be.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final SelectOutfitRepository selectOutfitRepository;
    private final SelectRecordRepository selectRecordRepository;

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(review -> {

                    String userNickname = userRepository.findById(review.getUserSeq())
                            .map(user -> user.getUserNickname())
                            .orElse("Unknown User");

                    // Fetch Join을 사용하여 selectOutfits와 관련된 outfit 정보 한 번에 가져오기
                    List<OutfitResponseDTO> outfits = selectOutfitRepository.findBySelectRecord_SelectSeqWithOutfit(review.getSelectSeq()).stream()
                            .map(selectOutfit -> new OutfitResponseDTO(
                                    selectOutfit.getOutfit().getOutfitSeq(),
                                    selectOutfit.getOutfit().getOutfitName()
                            ))
                            .collect(Collectors.toList());


                    return new ReviewDTO(
                            review.getReviewSeq(),
                            review.getUserSeq(),
                            review.getSelectSeq(),
                            userNickname,
                            review.getReviewLocation(),
                            review.getReviewWeather(),
                            review.getReviewContent(),
                            review.getReviewBlind() ? "BLIND" : "ACTIVE",
                            review.getReviewLikeYn(),
                            review.getReviewBlind(),
                            review.getRegDate(),
                            outfits
                    );
                })
                .collect(Collectors.toList());

    }

    public List<ReviewDTO> getMyReviews() {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        if (userSeq == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String userNickname = userRepository.findById(userSeq)
                .map(user -> user.getUserNickname())
                .orElse("Unknown User");

        return reviewRepository.findByUserSeq(userSeq).stream()
                .map(review -> {
                    List<OutfitResponseDTO> outfits = selectOutfitRepository.findBySelectRecord_SelectSeqWithOutfit(review.getSelectSeq()).stream()
                            .map(selectOutfit -> new OutfitResponseDTO(
                                    selectOutfit.getOutfit().getOutfitSeq(),
                                    selectOutfit.getOutfit().getOutfitName()
                            ))
                            .collect(Collectors.toList());

                    return new ReviewDTO(
                            review.getReviewSeq(),
                            review.getUserSeq(),
                            review.getSelectSeq(),
                            userNickname,
                            review.getReviewLocation(),
                            review.getReviewWeather(),
                            review.getReviewContent(),
                            review.getReviewBlind() ? "BLIND" : "ACTIVE",
                            review.getReviewLikeYn(),
                            review.getReviewBlind(),
                            review.getRegDate(),
                            outfits
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDto) {
        if (!selectRecordRepository.existsById(reviewDto.getSelectSeq())) {
            throw new CustomException(ErrorCode.INVALID_SELECT_SEQ);
        }

        Review review = new Review(
                reviewDto.getUserSeq(),
                reviewDto.getSelectSeq(),
                reviewDto.getReviewContent(),
                reviewDto.getWeather(),
                reviewDto.getLocation(),
                "BLIND".equals(reviewDto.getReviewStatus()),
                false
        );
        review = reviewRepository.save(review);

        String userNickname = userRepository.findById(review.getUserSeq())
                .map(user -> user.getUserNickname())
                .orElse("Unknown User");

        // Fetch Join을 사용하여 selectOutfits와 관련된 outfit 정보 한 번에 가져오기
        List<OutfitResponseDTO> outfits = selectOutfitRepository.findBySelectRecord_SelectSeqWithOutfit(review.getSelectSeq()).stream()
                .map(selectOutfit -> new OutfitResponseDTO(
                        selectOutfit.getOutfit().getOutfitSeq(),
                        selectOutfit.getOutfit().getOutfitName()
                ))
                .collect(Collectors.toList());

        return new ReviewDTO(
                review.getReviewSeq(),
                review.getUserSeq(),
                review.getSelectSeq(),
                userNickname,
                review.getReviewLocation(),
                review.getReviewWeather(),
                review.getReviewContent(),
                review.getReviewBlind() ? "BLIND" : "ACTIVE",
                review.getReviewLikeYn(),
                review.getReviewBlind(),
                review.getRegDate(),
                outfits
        );
    }

    @Transactional
    public ReviewDTO updateReview(Long reviewSeq, ReviewDTO reviewDto) {
        Review review = reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        review.updateReview(
                reviewDto.getReviewContent(),
                reviewDto.getWeather(),
                reviewDto.getLocation(),
                "BLIND".equals(reviewDto.getReviewStatus()),
                review.getReviewLikeYn()
        );

        String userNickname = userRepository.findById(review.getUserSeq())
                .map(user -> user.getUserNickname())
                .orElse("Unknown User");

        // Fetch Join을 사용하여 selectOutfits와 관련된 outfit 정보 한 번에 가져오기
        List<OutfitResponseDTO> outfits = selectOutfitRepository.findBySelectRecord_SelectSeqWithOutfit(review.getSelectSeq()).stream()
                .map(selectOutfit -> new OutfitResponseDTO(
                        selectOutfit.getOutfit().getOutfitSeq(),
                        selectOutfit.getOutfit().getOutfitName()
                ))
                .collect(Collectors.toList());

        return new ReviewDTO(
                review.getReviewSeq(),
                review.getUserSeq(),
                review.getSelectSeq(),
                userNickname,
                review.getReviewLocation(),
                review.getReviewWeather(),
                review.getReviewContent(),
                review.getReviewBlind() ? "BLIND" : "ACTIVE",
                review.getReviewLikeYn(),
                review.getReviewBlind(),
                review.getRegDate(),
                outfits
        );
    }

    @Transactional
    public void deleteReview(Long reviewSeq) {
        Review review = reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));
        reviewRepository.delete(review);
    }
}
