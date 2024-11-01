package article1be.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDTO {

    private long boardSeq;              // 게시물 번호
    private String boardTitle;          // 게시물 제목
    private String boardContent;        // 게시물 내용
    private LocalDateTime regDate;      // 게시물 등록일
    private boolean isBlind;            // 게시물 블라인드 여부
    private boolean isNotice;           // 게시물 공지 여부
}
