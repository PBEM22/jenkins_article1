package article1be.admin.controller;

import article1be.admin.service.AdminBoardService;
import article1be.board.entity.Board;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/board")
@AllArgsConstructor
@Slf4j
public class AdminBoardController {

    private final AdminBoardService service;

    @Operation(
            summary = "게시글 블라인드 설정",
            description = "관리자가 게시글의 블라인드를 설정"
    )
    @PutMapping(value = "/setting")
    public ResponseEntity<Board> releaseBoard(
            @RequestBody long boardSeq,
            @RequestBody boolean isBlind
    ) {
        Board board = service.settingBoard(boardSeq, isBlind);

        return ResponseEntity.ok(board);
    }
}