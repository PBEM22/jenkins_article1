package article1be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectOutfitResponseDTO {
    private Long selectSeq;

    private Long topSeq;
    private String topName;

    private Long bottomSeq;
    private String bottomName;

    private Long shoesSeq;
    private String shoesName;

    private Long outerSeq;
    private String outerName;

    private List<Long> accessorySeq;
    private List<String> accessoryNames;

}
