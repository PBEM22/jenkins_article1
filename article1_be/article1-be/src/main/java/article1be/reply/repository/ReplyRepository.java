package article1be.reply.repository;

import article1be.reply.dto.ReplyDTO;
import article1be.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // 게시글 번호의 댓글 중 블라인드처리 되지 않은 댓글 조회
    public List<Reply> findByBoardSeqAndReplyIsBlindFalse(Long boardSeq);

    // 블라인드 처리된 전체 댓글 조회
    List<Reply> findByReplyIsBlindTrue();
}
