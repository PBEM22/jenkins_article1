package article1be.reply.service;

import article1be.reply.entity.Reply;
import article1be.reply.repository.ReplyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {

    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ReplyService service;

    @Test
    @DisplayName("t1: 댓글 목록 조회 테스트")
    public void t1() {
        // Mock 데이터 생성
        List<Reply> mockReplyList = new ArrayList<>();
        mockReplyList.add(new Reply());

        // Mock 설정
        Mockito.when(replyRepository.findByBoardSeqAndReplyIsBlindFalse(1L)).thenReturn(mockReplyList);

        // Assertions
        Assertions.assertNotNull(service.getReplyList(1L));
        Assertions.assertEquals(mockReplyList.size(), service.getReplyList(1L).size());
        Assertions.assertEquals(mockReplyList.get(0).getBoardSeq(), service.getReplyList(1L).get(0).getBoardSeq());
    }

    @Test
    @DisplayName("t2: 블라인드 처리된 전체 댓글 조회")
    public void t2() {
        // Mock 데이터 생성
        List<Reply> mockReplyList = new ArrayList<>();
        mockReplyList.add(new Reply());

        Mockito.when(replyRepository.findByReplyIsBlindTrue()).thenReturn(mockReplyList);

        // Assertions
        Assertions.assertNotNull(service.getReplyList(-999L));
        Assertions.assertEquals(mockReplyList.size(), service.getReplyList(-999L).size());
    }
}
