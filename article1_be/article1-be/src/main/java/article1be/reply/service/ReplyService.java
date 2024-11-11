package article1be.reply.service;

import article1be.reply.dto.ReplyDTO;
import article1be.reply.dto.RequestReply;
import article1be.reply.entity.Reply;
import article1be.reply.repository.ReplyRepository;
import article1be.security.util.SecurityUtil;
import article1be.user.entity.UserAuth;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ReplyService {

    private final ReplyRepository repository;

    private final long FIND_ALL = -999L;

    // 댓글 목록 조회
    public List<ReplyDTO> getReplyList(long boardSeq) {
        if (boardSeq != FIND_ALL) {
            List<ReplyDTO> replyDTOList = repository.findByBoardSeqAndReplyIsBlindFalse(boardSeq)
                    .stream()
                    .map(reply ->
                            ReplyDTO.builder()
                                    .replySeq(reply.getReplySeq())
                                    .boardSeq(reply.getBoardSeq())
                                    .replyUserSeq(reply.getReplyUserSeq())
                                    .replyContent(reply.getReplyContent())
                                    .regDate(reply.getRegDate())
                                    .delDate(reply.getDelDate())
                                    .replyIsBlind(reply.isReplyIsBlind())
                                    .build())
                    .toList();

            return replyDTOList;
        } else {
            List<ReplyDTO> replyDTOList = repository.findByReplyIsBlindTrue()
                    .stream()
                    .map(reply ->
                            ReplyDTO.builder()
                                    .replySeq(reply.getReplySeq())
                                    .boardSeq(reply.getBoardSeq())
                                    .replyUserSeq(reply.getReplyUserSeq())
                                    .replyContent(reply.getReplyContent())
                                    .regDate(reply.getRegDate())
                                    .delDate(reply.getDelDate())
                                    .replyIsBlind(reply.isReplyIsBlind())
                                    .build())
                    .toList();

            return replyDTOList;
        }
    }

    // 댓글 삭제
    @Transactional
    public boolean deleteReply(Long replySeq) {
        Reply reply = repository.findById(replySeq).orElse(null);

        if (reply != null) {
            if (Objects.equals(reply.getReplyUserSeq(), SecurityUtil.getCurrentUserSeq()) || SecurityUtil.getCurrentUserAuthorities().equals(UserAuth.ADMIN)) {
                reply.setBlind();
                reply.setDelDate();

                return true;
            }
        } else {
            return false;
        }

        return false;
    }

    // 댓글 생성
    @Transactional
    public Reply createReply(Long boardSeq, RequestReply requestReply) {
        ReplyDTO replyDTO = new ReplyDTO(
                1L,             // DB에서 바꿔줌
                boardSeq,
                SecurityUtil.getCurrentUserSeq(),
                requestReply.getReplyContent(),
                LocalDateTime.now(),
                null,
                false
        );

        Reply reply = new Reply().create(replyDTO);

        return repository.save(reply);
    }
}
