package article1be.board.dto;

import article1be.board.entity.Picture;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PictureDTO {

    private long pictureSeq;
    private long pictureBoardSeq;
    private String pictureOriginName;
    private String pictureChangedName;
    private String pictureUrl;
    private String pictureType;
    private LocalDateTime regDate;
    private LocalDateTime delDate;
    private boolean pictureIsDelete;
}
