package article1be.blame.service;

import article1be.blame.entity.Blame;
import article1be.blame.exception.AlreadyReportedException;
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
import org.mockito.Mockito;
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
    @DisplayName("t1: 게시글 신고 테스트 - 정상 케이스")
    public void t1() {
        // 1번 유저가 1번 게시글 신고
        // Mock 설정
        Mockito.when(repository.save(Mockito.any())).thenReturn(new Blame());

        Assertions.assertDoesNotThrow(() -> {
            blameService.createBoardBlame(1L, 1L);
        });

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("t2: 댓글 신고 테스트 - 정상 케이스")
    public void t2() {
        // 1번 유저가 1번 댓글 신고
        Mockito.when(repository.save(Mockito.any())).thenReturn(new Blame());

        Assertions.assertDoesNotThrow(() -> {
            blameService.createReplyBlame(1L, 1L);
        });

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("t3: 리뷰 신고 테스트 - 정상 케이스")
    public void t3() {
        // 1번 유저가 1번 리뷰 신고
        Mockito.when(repository.save(Mockito.any())).thenReturn(new Blame());

        Assertions.assertDoesNotThrow(() -> {
            blameService.createReviewBlame(1L, 1L);
        });

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }

    // 추가 테스트: 이미 신고된 게시글에 대한 테스트
    @Test
    @DisplayName("t4: 이미 신고된 게시글 신고 테스트 - 예외 케이스")
    public void t4() {
        // Mock 설정
        Mockito.when(repository.existsByUserIdAndBoardId(1L, 1L)).thenReturn(true);

        Assertions.assertThrows(AlreadyReportedException.class, () -> {
            blameService.createBoardBlame(1L, 1L);
        });
    }

}