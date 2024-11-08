package article1be.board.controller;

import article1be.board.dto.BoardDTO;
import article1be.board.dto.RequestBoard;
import article1be.board.entity.Board;
import article1be.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Tag(
        name = "게시판",
        description = "게시글 관련 API"
)
@RestController
@RequestMapping(value = "board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService service;

    @Value("${s3.credentials.access-key}")
    private String accessKey;

    @Value("${s3.credentials.secret-key}")
    private String secretKey;

    @Value("${s3.credentials.region}")
    private String region;

    // 게시글 목록 조회
    @Operation(
            summary = "게시글 목록 조회",
            description = "블라인드 여부에 맞는 게시글 목록 반환(기본값: 블라인드 되지 않은)"
    )
    @GetMapping
    public ResponseEntity<List<BoardDTO>> getBoard(@RequestParam(defaultValue = "false") boolean isBlind) {

        return ResponseEntity.ok(this.service.getBoardList(isBlind));
    }

    // 게시글 조회
    @Operation(
            summary = "특정 게시글 상세 조회",
            description = "게시글의 번호, 작성자, 제목, 내용, 첨부 사진 목록, 작성일, 수정일, 삭제일, 블라인드 여부, 공지 여부를 반환"
    )
    @GetMapping(value = "/{boardSeq}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable("boardSeq") Long boardSeq) {

        // 조회 실패
        if (service.getBoard(boardSeq) == null) {
            return ResponseEntity.notFound().build();
        }
        // 조회 성공
        else {
            return ResponseEntity.ok(this.service.getBoard(boardSeq));
        }
    }

    // 게시글 등록
    @Operation(
            summary = "게시글 등록",
            description = "새로운 게시글의 제목, 내용, 첨부 사진들을 입력받아 새로운 게시글 등록"
    )
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Long> createBoard(
            @RequestParam("boardTitle") String boardTitle,
            @RequestParam("boardContent") String boardContent,
            @RequestParam(value = "imageList", required = false) List<MultipartFile> imageList
    ) {
        try {
            // RequestBoard 객체 생성
            RequestBoard newBoard = new RequestBoard();
            newBoard.setBoardTitle(boardTitle);
            newBoard.setBoardContent(boardContent);
            if (imageList != null && !imageList.isEmpty()) {
                newBoard.setImageList(imageList);
            }

            log.info("boardTitle = " + newBoard.getBoardTitle());
            log.info("boardContent = " + newBoard.getBoardContent());

            // 게시글 생성
            Board createdBoard = service.createBoard(newBoard);

            return ResponseEntity.ok(createdBoard.getBoardSeq());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 게시글 삭제
    @Operation(
            summary = "게시글 삭제",
            description = "게시글 삭제(작성자 또는 관리자만 가능)"
    )
    @DeleteMapping(value = "/{boardSeq}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardSeq") Long boardSeq) {
        boolean isDeleted = service.deleteBoard(boardSeq);

        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content 반환
        } else {
            // 게시글이 존재하지 않거나 삭제 실패 시
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404 Not Found
                    .body("게시글을 찾을 수 없습니다."); // 메시지 본문에 추가
        }
    }

    // 게시글 수정
    @Operation(
            summary = "게시글 수정",
            description = "등록된 게시글의 제목, 내용, 첨부 사진을 수정(작성자만 가능)"
    )
    @PutMapping(value = "/{boardSeq}")
    public ResponseEntity<String> updateBoard(
            @PathVariable("boardSeq") Long boardSeq,
            @RequestBody RequestBoard modData
    ) {
        try {
            Board updatedBoard = service.upDateBoard(boardSeq, modData);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/board/{boardSeq}")
                    .buildAndExpand(updatedBoard.getBoardSeq())
                    .toUri();

            return ResponseEntity.created(location).body("게시글이 성공적으로 수정되었습니다.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 수정 중 오류가 발생했습니다.");
        }
    }

    // 게시글 블라인드 해제
    @Operation(
            summary = "게시글 블라인드 해제",
            description = "관리자가 게시글의 블라인드를 해제"
    )
    @PutMapping(value = "/release/{boardSeq}")
    public ResponseEntity<Board> releaseBoard(@PathVariable long boardSeq) {
        Board board = service.releaseBoard(boardSeq);

        return ResponseEntity.ok(board);
    }
}
