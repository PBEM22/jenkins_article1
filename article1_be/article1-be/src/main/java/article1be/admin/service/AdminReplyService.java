package article1be.admin.service;

import article1be.reply.entity.Reply;
import article1be.reply.repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class AdminReplyService {

    private final ReplyRepository repository;

    public Reply settingReply(long replySeq, boolean isBlind) {

        // 댓글 조회
        Reply reply = repository.findById(replySeq)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다. ID: " + replySeq));

        if (isBlind) {
            reply.setBlind(); // 블라인드 설정
        } else {
            reply.unBlind(); // 블라인드 해제
        }

        // 변경된 댓글 저장
        return repository.save(reply);
    }
}
