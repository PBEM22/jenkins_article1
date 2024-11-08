package article1be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectRecordResponseDTO {
    private Long selectSeq;
    private LocalDateTime selectDate;
    private LocalDateTime customDate;
    private String customLocation;
    private double curTemp;


}
