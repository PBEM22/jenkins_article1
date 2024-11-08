package article1be.admin.service;

import article1be.board.entity.Board;
import article1be.board.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminBoardService {

    private final BoardRepository boardRepository;

    public Board settingBoard(long boardSeq, boolean isBlind) {
        // 게시글 조회
        Board board = boardRepository.findById(boardSeq)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다. ID: " + boardSeq));

        if (isBlind) {
            board.setBlind(); // 블라인드 설정
        } else {
            board.unBlind(); // 블라인드 해제
        }

        // 변경된 게시글 저장
        return boardRepository.save(board);
    }
}
