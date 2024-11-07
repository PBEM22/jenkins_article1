package article1be.blame.service;

import article1be.blame.entity.Blame;
import article1be.blame.repository.BlameRepository;
import article1be.board.entity.Board;
import article1be.board.repository.BoardRepository;
import article1be.reply.repository.ReplyRepository;
import article1be.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BlameService {

    private static final Logger log = LoggerFactory.getLogger(BlameService.class);
    private final BlameRepository blameRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final ReviewRepository reviewRepository;

    // 게시글 신고
    @Transactional
    public void createBoardBlame(long boardSeq, long userSeq) {
        blameRepository.save(Blame.builder()
                .blameUserSeq(userSeq)
                .blameBoardSeq(boardSeq)
                .blameReplySeq(null)
                .blameReviewSeq(null)
                .build()
        );

        // 해당 게시글에 대한 신고 개수 확인
        long blameCount = blameRepository.findByBlameBoardSeq(boardSeq).stream().count();
        log.info(boardSeq + "번 게시글에 누적 신고 개수 = " + blameCount);

        // 신고 개수가 10개 이상이면 boardIsBlind 값을 true로 업데이트
        if (blameCount >= 10) {
            Optional<Board> blamedBoardList = boardRepository.findById(boardSeq);
            if (blamedBoardList.isPresent()) {
                Board blamedBoard = blamedBoardList.get();
                blamedBoard.setBlind(); // 블라인드 처리
                boardRepository.save(blamedBoard); // 변경사항 저장
            }
        }
    }

    // 댓글 신고
    @Transactional
    public void createReplyBlame(long replySeq, long userSeq) {
        blameRepository.save(Blame.builder()
                .blameUserSeq(userSeq)
                .blameBoardSeq(null)
                .blameReplySeq(replySeq)
                .blameReviewSeq(null)
                .build()
        );

        // 해당 댓글에 대한 신고 개수 확인
        long blameCount = blameRepository.findByBlameReplySeq(replySeq).stream().count();
        log.info(replySeq + "번 댓글에 누적 신고 개수 = " + blameCount);

        // 신고 개수가 10개 이상이면 boardIsBlind 값을 true로 업데이트
        if (blameCount >= 10) {
            Optional<Board> blamedBoardList = boardRepository.findById(replySeq);
            if (blamedBoardList.isPresent()) {
                Board blamedBoard = blamedBoardList.get();
                blamedBoard.setBlind(); // 블라인드 처리
                boardRepository.save(blamedBoard); // 변경사항 저장
            }
        }
    }

    // 리뷰 신고
    @Transactional
    public void createReviewBlame(long reviewSeq, long userSeq) {
        blameRepository.save(Blame.builder()
                .blameUserSeq(userSeq)
                .blameBoardSeq(null)
                .blameReplySeq(null)
                .blameReviewSeq(reviewSeq)
                .build()
        );

        // 해당 댓글에 대한 신고 개수 확인
        long blameCount = blameRepository.findByBlameReviewSeq(reviewSeq).stream().count();
        log.info(reviewSeq + "번 리뷰에 누적 신고 개수 = " + blameCount);

        // 신고 개수가 10개 이상이면 boardIsBlind 값을 true로 업데이트
        if (blameCount >= 10) {
            Optional<Board> blamedBoardList = boardRepository.findById(reviewSeq);
            if (blamedBoardList.isPresent()) {
                Board blamedBoard = blamedBoardList.get();
                blamedBoard.setBlind(); // 블라인드 처리
                boardRepository.save(blamedBoard); // 변경사항 저장
            }
        }
    }
}
