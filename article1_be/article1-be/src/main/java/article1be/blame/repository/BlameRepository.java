package article1be.blame.repository;

import article1be.blame.entity.Blame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlameRepository extends JpaRepository<Blame, Long> {
    List<Blame> findByBlameBoardSeq(long boardSeq);

    List<Blame> findByBlameReplySeq(long replySeq);

    List<Blame> findByBlameReviewSeq(long reviewSeq);

    boolean existsByUserSeqAndBoardSeq(long userSeq, long boardSeq);
}
