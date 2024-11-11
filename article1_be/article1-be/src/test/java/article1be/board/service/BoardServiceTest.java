package article1be.board.service;

import article1be.amazonS3.service.AmazonS3Service;
import article1be.board.dto.BoardDTO;
import article1be.board.dto.RequestBoard;
import article1be.board.entity.Board;
import article1be.board.repository.BoardRepository;
import article1be.board.repository.PictureRepository;
import article1be.reply.repository.ReplyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private PictureRepository pictureRepository;
    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private AmazonS3Service amazonS3Service;

    @InjectMocks
    private BoardService service;

    @Test
    @DisplayName("t1: 게시글 목록 조회 테스트")
    public void t1() {
        // Mock 데이터 생성
        List<Board> mockBoardList = new ArrayList<>();

        // Mock 게시글 데이터 생성 및 필드 초기화
        Board mockBoard1 = new Board();
        mockBoard1.setBoardSeqAndUserSeqAndIsBlindAndIsNotice(1L, 1L, false, false); // 필요한 필드 초기화
        mockBoardList.add(mockBoard1); // 테스트 데이터 1

        Board mockBoard2 = new Board();
        mockBoard2.setBoardSeqAndUserSeqAndIsBlindAndIsNotice(2L, 2L, false, false); // 필요한 필드 초기화
        mockBoardList.add(mockBoard2); // 테스트 데이터 2

        // Mock 설정
        Mockito.when(boardRepository.findByBoardIsBlindFalse()).thenReturn(mockBoardList);

        // 실제 메소드 호출
        List<BoardDTO> result = service.getBoardList(false);

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockBoardList.size(), result.size());

        // 추가 Assertions: 각 게시글의 boardSeq가 올바르게 설정되었는지 확인
        Assertions.assertEquals(mockBoardList.get(0).getBoardSeq(), result.get(0).getBoardSeq());
        Assertions.assertEquals(mockBoardList.get(1).getBoardSeq(), result.get(1).getBoardSeq());
    }


    @Test
    @DisplayName("t2: 게시글 조회 테스트")
    public void t2() {
        // Mock 설정
        Board mockBoard = new Board();
        mockBoard.setBoardSeqAndUserSeqAndIsBlindAndIsNotice(1L, 1L, false, false);
        Mockito.when(boardRepository.findById(1L)).thenReturn(Optional.of(mockBoard));

        // Assertions
        Assertions.assertNotNull(service.getBoard(1L));
        Assertions.assertEquals(1L, service.getBoard(1L).getBoardSeq());
    }

    @Test
    @DisplayName("t3: 블라인드 게시글 목록 조회 테스트")
    public void t3() {
        // Mock 데이터 생성
        List<Board> mockBoardList = new ArrayList<>();

        // Mock 게시글 데이터 생성 및 필드 초기화
        Board mockBoard1 = new Board();
        mockBoard1.setBoardSeqAndUserSeqAndIsBlindAndIsNotice(1L, 1L, true, false); // 필요한 필드 초기화
        mockBoardList.add(mockBoard1); // 테스트 데이터 1

        Board mockBoard2 = new Board();
        mockBoard2.setBoardSeqAndUserSeqAndIsBlindAndIsNotice(2L, 2L, true, false); // 필요한 필드 초기화
        mockBoardList.add(mockBoard2); // 테스트 데이터 2

        // Mock 설정
        Mockito.when(boardRepository.findByBoardIsBlindFalse()).thenReturn(mockBoardList);

        // 실제 메소드 호출
        List<BoardDTO> result = service.getBoardList(false);

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockBoardList.size(), result.size());

        // 추가 Assertions: 각 게시글의 boardSeq가 올바르게 설정되었는지 확인
        Assertions.assertEquals(mockBoardList.get(0).getBoardSeq(), result.get(0).getBoardSeq());
        Assertions.assertEquals(mockBoardList.get(1).getBoardSeq(), result.get(1).getBoardSeq());
    }

    @Test
    @DisplayName("t4: 게시글 수정")
    public void t4() throws IOException {
        // Mock 설정
        RequestBoard requestBoard = new RequestBoard();
        requestBoard.setBoardTitle("새로운 글!");
        requestBoard.setBoardContent("새로운 내용");
        requestBoard.setImageList(null);

        // Mocking the update behavior
        Mockito.when(boardRepository.findById(1L)).thenReturn(Optional.of(new Board()));
        Mockito.when(boardRepository.save(Mockito.any(Board.class))).thenReturn(new Board());

        // 실제 메소드 호출
        Board board = service.upDateBoard(1L, requestBoard);

        // Assertions
        Assertions.assertNotNull(board, "게시글 수정 실패: null 반환");
        System.out.println(board);
    }

    @Test
    @DisplayName("t5: 작성자 또는 관리자가 아닌 유저가 게시글 삭제")
    public void t5() {
        // Mock 설정: 게시글이 존재하는 경우
        Board mockBoard = new Board();
        mockBoard.setBoardSeqAndUserSeqAndIsBlindAndIsNotice(1L, 1L, false, false); // 게시글 ID 설정

        // findById가 mockBoard를 반환하도록 설정
        Mockito.when(boardRepository.findById(1L)).thenReturn(Optional.of(mockBoard));
        // 게시글 삭제를 위한 설정

        // 실제 메소드 호출
        boolean result = service.deleteBoard(1L);

        // Assertions
        Assertions.assertFalse(result, "게시글 삭제가 성공해야 합니다.");
    }
}
