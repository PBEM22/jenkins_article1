package article1be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreferenceResponseDTO {

    private Long styleSeq;       // 스타일 고유번호
    private Long conditionSeq;   // 체질 고유번호

}
