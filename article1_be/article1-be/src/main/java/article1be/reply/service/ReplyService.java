package article1be.reply.service;

import article1be.reply.dto.ReplyDTO;
import article1be.reply.dto.RequestReply;
import article1be.reply.entity.Reply;
import article1be.reply.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReplyService {

    private final ReplyRepository repository;

    // 댓글 목록 조회
    public List<ReplyDTO> getReplyList(long boardSeq) {
        List<ReplyDTO> replyDTOList = repository.findByBoardSeqAndReplyIsBlindFalse(boardSeq)
                .stream()
                .map(reply ->
                        ReplyDTO.builder()
                                .boardSeq(reply.getBoardSeq())
                                .replyUserSeq(reply.getReplyUserSeq())
                                .replyContent(reply.getReplyContent())
                                .regDate(reply.getRegDate())
                                .delDate(reply.getDelDate())
                                .replyIsBlind(reply.getReplyIsBlind())
                                .build())
                .toList();

        return replyDTOList;
    }

    // 댓글 삭제
    @Transactional
    public boolean deleteReply(Long replySeq) {
        Reply reply = repository.findById(replySeq).orElse(null);

        if (reply != null) {
            reply.setBlind();

            return true;
        } else {
            return false;
        }
    }

    // 댓글 생성
    @Transactional
    public Reply createReply(Long boardSeq, RequestReply requestReply) {
        ReplyDTO replyDTO = new ReplyDTO(
                boardSeq,
                123L,
                requestReply.getReplyContent(),
                LocalDateTime.now(),
                null,
                false
        );

        Reply reply = new Reply().create(replyDTO);

        return repository.save(reply);
    }
}
