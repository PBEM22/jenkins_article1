package article1be.board.repository;

import article1be.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 블라인드인드 되지 않은 값을 조회
    List<Board> findByIsBlindFalse();
}
