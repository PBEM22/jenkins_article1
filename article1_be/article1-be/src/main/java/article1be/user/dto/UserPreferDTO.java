package article1be.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPreferDTO {

    @NotNull(message = "스타일은 필수 입력값입니다.")
    private Long styleSeq;

    @NotNull(message = "체질은 필수 입력값입니다.")
    private Long conditionSeq;

}
