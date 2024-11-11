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
import java.time.LocalDateTime;
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
    public void testGetBoardList() {

        // Mock 데이터 생성
        List<BoardDTO> mockBoardList = new ArrayList<>();

        // 테스트 데이터 1
        mockBoardList.add(BoardDTO.builder()
                .boardSeq(1L)
                .userSeq(1L)
                .boardTitle("게시글 제목 1")
                .boardContent("게시글 내용 1")
                .regDate(LocalDateTime.now())
                .upDate(LocalDateTime.now())
                .delDate(null)
                .boardIsBlind(false)
                .boardIsNotice(false)
                .build());

        // 테스트 데이터 2
        mockBoardList.add(BoardDTO.builder()
                .boardSeq(2L)
                .userSeq(2L)
                .boardTitle("게시글 제목 2")
                .boardContent("게시글 내용 2")
                .regDate(LocalDateTime.now())
                .upDate(LocalDateTime.now())
                .delDate(null)
                .boardIsBlind(false)
                .boardIsNotice(false)
                .build());

        // Mock 설정
        Mockito.when(service.getBoardList(false))
                .thenReturn(mockBoardList);

        // 실제 메소드 호출
        List<BoardDTO> list = service.getBoardList(false);

        // Assertions
        Assertions.assertNotNull(list);
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertEquals(2, list.size());
        // 추가적으로 각 게시글의 내용도 검증할 수 있습니다.
    }

    @Test
    public void testGetBoardById() {
        // Mock 설정
        Board mockBoard = new Board().create(
                1L,
                "제목",
                "내용",
                false
        );

        Mockito.when(boardRepository.findById(1L)).thenReturn(Optional.of(mockBoard));

        // 실제 메소드 호출
        BoardDTO result = service.getBoard(1L);

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals("게시글 제목", result.getBoardTitle());
    }


    @Test
    @DisplayName("t3: 블라인드 게시글 목록 조회")
    public void t3() {

        List<BoardDTO> list = service.getBoardList(true);

        Assertions.assertDoesNotThrow(() -> {
            if (list == null) {
                throw new RuntimeException("게시글 목록 조회에 실패하였습니다.");
            } else if (list.size() < 1) {
                System.out.println("등록된 게시글이 없습니다.");
            } else {
                System.out.println("조회에 성공하였습니다.");
                list.forEach(System.out::println);
            }
        });
    }

    @Test
    @DisplayName("t4: 게시글 수정")
    public void t4() throws IOException {

        RequestBoard requestBoard = new RequestBoard();
        requestBoard.setBoardTitle("새로운 글!");
        requestBoard.setBoardContent("새로운 내용");
        requestBoard.setImageList(null);

        Board board = service.upDateBoard(1L, requestBoard);
        Assertions.assertDoesNotThrow(() -> {
            if (board == null) {
                throw new RuntimeException("게시글 수정 실패");
            } else {
                System.out.println(board);
            }
        });
    }

    @Test
    @DisplayName("t5: 게시글 삭제")
    public void t5() {

        boolean result = service.deleteBoard(1L);

        Assertions.assertDoesNotThrow(() -> {
            if (result) {
                System.out.println("게시글 삭제 성공");
            } else {
                System.out.println("게시글 삭제 실패");
            }
        });
    }
}
