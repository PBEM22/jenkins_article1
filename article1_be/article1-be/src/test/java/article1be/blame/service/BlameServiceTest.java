package article1be.blame.service;

import article1be.blame.repository.BlameRepository;
import article1be.board.repository.BoardRepository;
import article1be.reply.repository.ReplyRepository;
import article1be.review.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlameServiceTest {

    @Mock
    private BlameRepository repository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private BlameService blameService;

    @Test
    @DisplayName("t1: 게시글 신고 테스트")
    public void t1() {
        // 1번 유저가 1번 게시글 신고
        Assertions.assertDoesNotThrow(() -> {
            blameService.createBoardBlame(1L, 1L);
        });
    }
}