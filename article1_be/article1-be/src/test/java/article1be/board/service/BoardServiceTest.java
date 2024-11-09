package article1be.board.service;

import article1be.board.dto.BoardDTO;
import article1be.board.dto.RequestBoard;
import article1be.board.entity.Board;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    private BoardService service;

    @Test
    @DisplayName("t1: 게시글 목록 조회 테스트")
    public void t1() {

        List<BoardDTO> list = service.getBoardList(false);

        Assertions.assertDoesNotThrow(() -> {
            if (list == null) {
                throw new RuntimeException("게시글 목록 조회에 실패하였습니다.");
            } else if (list.size() < 1) {
                System.out.println("등록된 게시글이 없습니다.");
            } else {
                System.out.println("조회에 성공하였습니다.");
                list.forEach(System.out::println);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("t2: 게시글 상세 조회 테스트")
    @ValueSource(longs = {1L, 2L})
    public void t2(long boardSeq) {

        BoardDTO board = service.getBoard(boardSeq);

        Assertions.assertDoesNotThrow(() -> {
            if (board == null) {
                throw new RuntimeException("해당 글이 존재하지 않습니다.");
            } else {
                System.out.println(board);
            }
        });
    }

    @Test
    @DisplayName("t3: 블라인드 게시글 목록 조회")
    public void t3() {

        List<BoardDTO> list = service.getBoardList(true);

        Assertions.assertDoesNotThrow(() -> {
            if (list == null) {
                throw new RuntimeException("게시글 목록 조회에 실패하였습니다.");
            } else if (list.size() < 1) {
                System.out.println("등록된 게시글이 없습니다.");
            } else {
                System.out.println("조회에 성공하였습니다.");
                list.forEach(System.out::println);
            }
        });
    }

    @Test
    @DisplayName("t4: 게시글 수정")
    public void t4() throws IOException {

        RequestBoard requestBoard = new RequestBoard();
        requestBoard.setBoardTitle("새로운 글!");
        requestBoard.setBoardContent("새로운 내용");
        requestBoard.setImageList(null);

        Board board = service.upDateBoard(1L, requestBoard);
        Assertions.assertDoesNotThrow(() -> {
            if (board == null) {
                throw new RuntimeException("게시글 수정 실패");
            } else {
                System.out.println(board);
            }
        });
    }

    @Test
    @DisplayName("t5: 게시글 삭제")
    public void t5() {

        boolean result = service.deleteBoard(1L);

        Assertions.assertDoesNotThrow(() -> {
            if (result) {
                System.out.println("게시글 삭제 성공");
            } else {
                System.out.println("게시글 삭제 실패");
            }
        });
    }
}
