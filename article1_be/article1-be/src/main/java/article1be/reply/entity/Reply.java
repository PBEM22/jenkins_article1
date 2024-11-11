package article1be.reply.entity;

import article1be.reply.dto.ReplyDTO;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "REPLY")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long replySeq;
    long boardSeq;
    long replyUserSeq;
    String replyContent;
    LocalDateTime regDate;
    LocalDateTime delDate;
    boolean replyIsBlind;

    public void setBlind() {
        this.replyIsBlind = true;
    }

    public void setDelDate() {
        this.delDate = LocalDateTime.now();
    }

    public Reply create(ReplyDTO replyDTO) {
        Reply reply = new Reply();


        reply.boardSeq = replyDTO.getBoardSeq();
        reply.replyUserSeq = replyDTO.getReplyUserSeq();
        reply.replyContent = replyDTO.getReplyContent();
        reply.regDate = replyDTO.getRegDate();
        reply.delDate = replyDTO.getDelDate();
        reply.replyIsBlind = replyDTO.isReplyIsBlind();

        return reply;
    }

    public void unBlind() {
        this.replyIsBlind = false;
    }
}
