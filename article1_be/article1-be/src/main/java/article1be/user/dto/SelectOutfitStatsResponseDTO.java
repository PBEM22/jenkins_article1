package article1be.user.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectOutfitStatsResponseDTO {
    private String startDate;
    private String endDate;
    private List<CategoryStatsDTO> categories;
}
