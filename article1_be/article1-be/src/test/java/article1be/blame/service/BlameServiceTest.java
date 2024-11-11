package article1be.blame.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class BlameServiceTest {

    @Autowired
    BlameService service;

    @Test
    @DisplayName("t1: 게시글 신고 테스트")
    public void t1() {
        Assertions.assertDoesNotThrow(() -> {
            service.createBoardBlame(1L, 11L);
        });
    }

    @Test
    @DisplayName("t2: 댓글 신고 테스트")
    public void t2() {
        Assertions.assertDoesNotThrow(() -> {
            service.createReplyBlame(2L, 15L);
        });
    }

    @Test
    @DisplayName("t2: 리뷰 신고 테스트")
    public void t3() {
        Assertions.assertDoesNotThrow(() -> {
            service.createReviewBlame(3L, 7L);
        });
    }
}