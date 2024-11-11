package article1be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryStatsDTO {
    private String category; // 카테고리 이름
    private List<OutfitStatsDTO> outfits; // 해당 카테고리의 옷 정보 리스트
}
