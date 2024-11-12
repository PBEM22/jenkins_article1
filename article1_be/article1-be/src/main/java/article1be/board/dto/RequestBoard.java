package article1be.board.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RequestBoard {

    private String boardTitle;
    private String boardContent;
    private List<MultipartFile> imageList;
}
