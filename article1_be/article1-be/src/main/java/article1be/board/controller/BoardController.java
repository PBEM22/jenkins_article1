package article1be.board.controller;

import article1be.board.dto.BoardDTO;
import article1be.board.dto.RequestBoard;
import article1be.board.entity.Board;
import article1be.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "board")
public class BoardController {

    private final BoardService service;

    @Autowired
    public BoardController(BoardService service) {
        this.service = service;
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<List<BoardDTO>> getBoard() {

        return ResponseEntity.ok(this.service.getBoardList());
    }

    // 게시글 조회
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

    @PostMapping
    public ResponseEntity<String> createBoard(@RequestBody RequestBoard newBoard) {
        try {
            // 게시글 생성
            Board createdBoard = service.createBoard(newBoard);

            // 생성된 게시글의 URI를 설정
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{boardSeq}")
                    .buildAndExpand(createdBoard.getBoardSeq())
                    .toUri();

            return ResponseEntity.created(location).body("게시글이 성공적으로 생성되었습니다.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 생성 중 오류가 발생했습니다.");
        }
    }


    // 게시글 삭제
    @DeleteMapping(value = "/{boardSeq}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardSeq") Long boardSeq) {
        service.deleteBoard(boardSeq);

        return ResponseEntity.noContent().build();
    }

    // 게시글 수정
    @PutMapping(value = "/{boardSeq}")
    public ResponseEntity<String> updateBoard(
            @PathVariable("boardSeq") Long boardSeq,
            @RequestBody RequestBoard modData
    ) {
        try {
            Board updatedBoard = service.upDateBoard(boardSeq, modData);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{boardSeq}")
                    .buildAndExpand(updatedBoard.getBoardSeq())
                    .toUri();

            return ResponseEntity.ok("게시글이 성공적으로 수정되었습니다.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 수정 중 오류가 발생했습니다.");
        }
    }
}
