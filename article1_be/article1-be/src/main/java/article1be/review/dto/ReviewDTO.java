package article1be.review.dto;

import article1be.outfit.dto.OutfitResponseDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long reviewSeq;         // 리뷰 번호
    private Long userSeq;           // 유저 번호
    private Long selectSeq;         // 선택 번호
    private String userNickname;    // 유저 닉네임
    private String location;        // 위치 정보
    private Double weather;         // 날씨 정보
    private String reviewContent;   // 리뷰 내용
    private String reviewStatus;    // 리뷰 상태 (ACTIVE 또는 BLIND)
    private boolean reviewLikeYn;    // 좋아요 유무
    private boolean reviewBlind;
    private LocalDateTime regDate;
    private LocalDateTime delDate;
    private List<OutfitResponseDTO> outfits; // 옷 정보 추가
}
