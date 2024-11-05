package article1be.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestPicture {
    private long pictureBoardSeq;
    private String pictureOriginName;
    private String pictureChangedName;
    private String pictureUrl;
    private String pictureType;
}
