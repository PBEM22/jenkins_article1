package article1be.admin.controller;

import article1be.admin.service.AdminReplyService;
import article1be.reply.entity.Reply;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 관련 API", description = "관리자 권한 사용자가 사용하는 API")
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/admin/reply")
public class AdminReplyController {

    private final AdminReplyService service;

    // 댓글 블라인드 해제
    @Operation(
            summary = "댓글 블라인드 설정",
            description = "관리자가 댓글의 블라인드를 설정"
    )
    @PutMapping(value = "/setting")
    public ResponseEntity<Reply> releaseReply(
            @RequestBody long replySeq,
            @RequestBody boolean isBlind
    ) {
        Reply reply = service.settingReply(replySeq, isBlind);

        return ResponseEntity.ok(reply);
    }
}
