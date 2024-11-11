package article1be.reply.service;

import article1be.board.dto.BoardDTO;
import article1be.board.dto.RequestBoard;
import article1be.board.entity.Board;
import article1be.reply.dto.ReplyDTO;
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
class ReplyServiceTest {

    @Autowired
    private ReplyService service;

    @Test
    @DisplayName("t1: 댓글 목록 조회 테스트")
    public void t1() {

        List<ReplyDTO> list = service.getReplyList(1L);

        Assertions.assertDoesNotThrow(() -> {
            if (list == null) {
                throw new RuntimeException("댓글 목록 조회에 실패하였습니다.");
            } else if (list.size() < 1) {
                System.out.println("등록된 댓글이 없습니다.");
            } else {
                System.out.println("조회에 성공하였습니다.");
                list.forEach(System.out::println);
            }
        });
    }

    @Test
    @DisplayName("t2: 댓글 삭제")
    public void t2() {

        boolean result = service.deleteReply(1L);

        Assertions.assertDoesNotThrow(() -> {
            if (result) {
                System.out.println("게시글 삭제 성공");
            } else {
                System.out.println("게시글 삭제 실패");
            }
        });
    }
}