package article1be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OutfitStatsDTO {
    private String category;
    private Long outfitSeq; // 옷 고유 번호
    private String outfitName; // 옷 이름
    private Long selectionCount; // 선택 횟수
    private Double percentage; // 카테고리 내 퍼센트

}
