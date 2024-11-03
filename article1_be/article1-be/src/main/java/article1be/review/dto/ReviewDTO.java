package article1be.review.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Long reviewSeq;         // 리뷰 번호
    private Long userSeq;           // 유저 번호
    private Long selectSeq;         // 선택 번호
    private String reviewContent;   // 리뷰 내용
    private Double reviewWeather;   // 날씨 정보
    private Double reviewLocation;  // 위치 정보
    private Boolean reviewBlind;    // 블라인드 여부
    private Boolean reviewLikeYn;   // 좋아요 여부
    private Integer reviewReport;   // 신고 개수
}
