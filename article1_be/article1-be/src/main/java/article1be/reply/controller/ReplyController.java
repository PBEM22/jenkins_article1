package article1be.reply.controller;

import article1be.reply.dto.ReplyDTO;
import article1be.reply.dto.RequestReply;
import article1be.reply.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/reply")
public class ReplyController {

    private final ReplyService service;

    @Autowired
    public ReplyController(ReplyService service) {
        this.service = service;
    }

    // 댓글 목록 조회
    @GetMapping(value = "/{boardSeq}")
    public ResponseEntity<List<ReplyDTO>> getReply(@PathVariable Long boardSeq) {

        // 조회결과 없음
        if (service.getReplyList(boardSeq) == null || service.getReplyList(boardSeq).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // 조회 성공
        else {
            return ResponseEntity.ok(service.getReplyList(boardSeq));
        }
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<String> createReply(@RequestBody RequestReply requestReply) {

        // 성공
        if (service.createReply(requestReply) != null) {
            return ResponseEntity.ok("댓글 작성이 완료되었습니다.");
        }
        // 실패
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 댓글 삭제
    @DeleteMapping(value = "/{replySeq}")
    public ResponseEntity<String> deleteReply(@PathVariable Long replySeq) {
        // 블라인드 처리 성공
        if (service.deleteReply(replySeq)) {
            return ResponseEntity.ok("삭제에 성공하였습니다."); // 성공 메시지
        }
        // 블라인드 처리 실패
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
