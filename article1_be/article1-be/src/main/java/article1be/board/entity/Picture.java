package article1be.board.entity;

import article1be.board.dto.RequestPicture;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "picture")
@Getter
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pictureSeq;
    private long pictureBoardSeq;
    private String pictureOriginName;
    private String pictureChangedName;
    private String pictureUrl;
    private String pictureType;
    private LocalDateTime regDate;
    private LocalDateTime delDate;
    private boolean pictureIsDelete;

    public void create(RequestPicture requestPicture) {
        this.pictureBoardSeq = requestPicture.getPictureBoardSeq();
        this.pictureOriginName = requestPicture.getPictureOriginName();
        this.pictureChangedName = requestPicture.getPictureChangedName();
        this.pictureUrl = requestPicture.getPictureUrl();
        this.pictureType = requestPicture.getPictureType();
        this.regDate = LocalDateTime.now();
        this.delDate = null;
        this.pictureIsDelete = false;
    }

    public void setBlind() {
        this.delDate = LocalDateTime.now();
        this.pictureIsDelete = true;
    }
}
