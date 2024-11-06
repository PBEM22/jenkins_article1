package article1be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreferenceResponseDTO {

    private String styleName;       // 스타일 이름
    private String conditionName;   // 체질 이름

}
