package article1be.admin.service;

import article1be.board.entity.Board;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class AdminBoardServiceTest {

    @Autowired
    private AdminBoardService service;

    @Test
    @DisplayName("t1: 게시글 블라인드 처리")
    public void t1() {

        Board board = service.settingBoard(1L, true);

        Assertions.assertDoesNotThrow(() -> {
            System.out.println(board);
        });
    }
}