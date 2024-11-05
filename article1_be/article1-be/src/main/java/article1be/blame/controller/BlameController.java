package article1be.blame.controller;

import article1be.blame.service.BlameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/report")
public class BlameController {

    private final BlameService service;

    // 게시판 신고
    @PostMapping(value = "/{boardSeq}")
    public ResponseEntity<String> blameBoard(@PathVariable("boardSeq") long boardSeq) {
        service.createBoardBlame(boardSeq);

        return ResponseEntity.ok("신고가 완료되었습니다.");
    }

    // 댓글 신고
    @PostMapping(value = "/{replySeq}")
    public ResponseEntity<String> blameReply(@PathVariable("replySeq") long replySeq) {
        service.createReplyBlame(replySeq);

        return ResponseEntity.ok("신고가 완료되었습니다.");
    }

    // 리뷰 신고
    @PostMapping(value = "/{reviewSeq}")
    public ResponseEntity<String> blameReview(@PathVariable("reviewSeq") long reviewSeq) {
        service.createReviewBlame(reviewSeq);

        return ResponseEntity.ok("신고가 완료되었습니다.");
    }
}
