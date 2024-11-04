package article1be.review.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long reviewSeq;         // 리뷰 번호
    private Long userSeq;           // 유저 번호
    private String userNickname;    // 유저 닉네임
    private String location;        // 위치 정보
    private String weather;         // 날씨 정보 (예: 10°C)
    private String reviewContent;   // 리뷰 내용
    private String reviewStatus;    // 리뷰 상태 (ACTIVE 또는 BLIND)
    private Integer reviewReport;   // 신고 개수
}
